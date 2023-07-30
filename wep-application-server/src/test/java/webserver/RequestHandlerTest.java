package webserver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
}