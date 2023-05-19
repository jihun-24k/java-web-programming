package webserver;

import static org.junit.Assert.assertEquals;

import db.DataBase;
import java.io.IOException;
import model.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class RequestHandlerTest {

    @Test
    public void response_Default() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/index.html");
        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void login_GET_Method() throws IOException {
        HttpGet request = new HttpGet("http://localhost:8080/create?userId=test&password=1234&name=gildong&email=test@example.com");
        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        User findUser = DataBase.findUserById("test");

        assertEquals("1234", findUser.getPassword());
        assertEquals("gildong", findUser.getName());
        assertEquals("test@example.com", findUser.getEmail());
    }
}