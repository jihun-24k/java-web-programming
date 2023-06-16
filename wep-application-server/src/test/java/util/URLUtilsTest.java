package util;


import static org.junit.Assert.*;

import org.junit.Test;

public class URLUtilsTest {

    @Test
    public void getURL() {

        //given
        String requestLine = "GET /index.html HTTP/1.1";

        //when
        String path = URLUtils.getURL(requestLine);

        //then
        assertEquals("/index.html", path);
    }

    @Test
    public void getParamQuery() {

        String url = "/user/form.html?id=abc123";
        String paramQuery = URLUtils.getParamQuery(url);
        assertEquals("id=abc123", paramQuery);
    }

    @Test
    public void getRequestPath(){

        String url = "/user/form.html?id=abc123";
        String requestPath = URLUtils.getRequestPath(url);
        assertEquals("/user/form.html", requestPath);
    }

}