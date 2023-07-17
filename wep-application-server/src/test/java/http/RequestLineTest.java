package http;

import static org.junit.Assert.*;

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

}