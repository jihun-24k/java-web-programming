package http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private DataOutputStream dos;
    private Map<String, String> header = new HashMap<>();

    private byte[] body = {};

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String url) throws IOException {
        body = Files.readAllBytes(new File("./webapp" + url).toPath());
        response200Header();
        responseBody();
    }

    public void sendRedirect(String url) {
        response302Header(url);
        responseBody();
    }

    private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080" + url + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
