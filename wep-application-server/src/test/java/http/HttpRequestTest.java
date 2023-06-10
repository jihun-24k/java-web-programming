package http;

import static junit.framework.TestCase.assertEquals;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import org.junit.Test;

public class HttpRequestTest {

    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() throws Exception {
        InputStream in = Files.newInputStream(new File(testDirectory + "Http_GET.txt").toPath());

        HttpRequest request = new HttpRequest(in);
        assertEquals("GET", request.getHttpMethod());
        assertEquals("user/create", request.getPath());
        assertEquals("keep-alive", request.getHeader("Connection"));
        assertEquals("jihun", request.getParameter("userId"));
    }
}
