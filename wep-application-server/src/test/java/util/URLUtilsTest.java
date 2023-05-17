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

}