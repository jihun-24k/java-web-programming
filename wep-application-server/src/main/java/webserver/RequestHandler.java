package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.URLUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferIn = new BufferedReader(new InputStreamReader(in));

            String url = readURL(bufferIn);

            String requestPath = URLUtils.getRequestPath(url);
            String queryParams = URLUtils.getParamQuery(url);

            if (queryParams != null) {
                Map<String, String> query = HttpRequestUtils.parseQueryString(queryParams);

                User joinUser = new User(
                    query.get("userId")
                    ,query.get("password")
                    ,query.get("name")
                    ,query.get("email")
                );

                DataBase.addUser(joinUser);
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + requestPath).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String readURL(BufferedReader bufferIn) throws IOException {
        String line = bufferIn.readLine();
        String url = URLUtils.getURL(line);
        log.debug("url = {}", url);

        while (!"".equals(line) && line != null) {
            log.debug("HTTP Header Info = {}", line);
            line = bufferIn.readLine();
        }
        return url;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
