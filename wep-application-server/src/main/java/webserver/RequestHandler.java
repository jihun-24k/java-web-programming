package webserver;

import db.DataBase;
import http.HttpRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
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

            HttpRequest httpRequest = new HttpRequest(bufferIn);

            String url = httpRequest.getUrl();
            String httpMethod = httpRequest.getHttpMethod();

            String requestPath = URLUtils.getRequestPath(url);
            String queryParams = URLUtils.getParamQuery(url);
            Map<String, String> cookies = HttpRequestUtils.parseCookies(httpRequest.getCookies());
            String requestBody = "";

            if (httpMethod.equals("POST")) {
                requestBody = IOUtils.readData(bufferIn, httpRequest.getContentLength());
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = responseHeader(dos, requestPath, requestBody, cookies);

            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] responseHeader(DataOutputStream dos, String requestPath, String requestBody, Map<String, String> cookies)
        throws IOException {
        byte[] body = {};
        Map<String, String> query = HttpRequestUtils.parseQueryString(requestBody);

        if (requestPath.equals("/user/create")) {
            saveUser(query);
            response302Header(dos);
        }
        else if (requestPath.endsWith(".css")) {
            body = Files.readAllBytes(new File("./webapp" + requestPath).toPath());
            response200CssHeader(dos, body.length);
        }
        else if (requestPath.equals("/user/login")) {
            if (canLogin(query)) {
                responseLoginSuccess(dos);
            }
            else {
                responseLoginFail(dos);
            }
        }
        else if (requestPath.equals("/user/list")) {
            String loginedCookie = cookies.get("logined");
            boolean logined = Boolean.parseBoolean(loginedCookie);
            if (logined) {
                Collection<User> users = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border = '1'>");
                for (User user : users) {
                    sb.append("<tr>");
                    sb.append("<td>" + user.getUserId() + "</td>");
                    sb.append("<td>" + user.getName() + "</td>");
                    sb.append("<td>" + user.getEmail() + "</td>");
                    sb.append("</tr>");
                }
                sb.append("</table>");
                body = sb.toString().getBytes();
                response200Header(dos, body.length);
            }
            else {
                responseLogin(dos);
            }
        }

        else {
            body = Files.readAllBytes(new File("./webapp" + requestPath).toPath());
            response200Header(dos, body.length);
        }

        return body;
    }

    private boolean canLogin(Map<String, String> query) {
        User findUser = DataBase.findUserById(query.get("userId"));

        if (findUser == null) {
            return false;
        }

        if (!findUser.getPassword().equals(query.get("password"))) {
            return false;
        }

        return true;
    }


    private void saveUser(Map<String, String> query) {

        User joinUser = new User(
                query.get("userId")
                ,query.get("password")
                ,query.get("name")
                ,query.get("email")
        );

        DataBase.addUser(joinUser);
    }

    private void responseLoginSuccess(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("Set-Cookie: logined=true; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseLoginFail(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/user/login_failed.html\r\n");
            dos.writeBytes("Set-Cookie: logined=false; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseLogin(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/user/login.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;\r\n");
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
