package http;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class RequestLineTest {

    @Test
    public void create_method() {
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");
        assertEquals("GET", line.getMethod());
        assertEquals("/index.html", line.getPath());

        line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals("POST", line.getMethod());
        assertEquals("/index.html", line.getPath());
    }

    @Test
    public void create_path_and_params() {
        RequestLine line = new RequestLine("GET /user/create?userId=jihun&password=1234 HTTP/1.1");
        assertEquals("GET", line.getMethod());
        assertEquals("/user/create", line.getPath());

        Map<String, String> params = line.getParams();
        assertEquals(2, params.size());
    }
}