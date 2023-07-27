package webserver;

import db.DataBase;
import http.HttpRequest;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            HttpRequest httpRequest = new HttpRequest(in);
            String path = getDefaultPath(httpRequest.getPath());

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = {};

            if (path.equals("/user/create")) {
                User joinUser = new User(
                    httpRequest.getParameter("userId")
                    ,httpRequest.getParameter("password")
                    ,httpRequest.getParameter("name")
                    ,httpRequest.getParameter("email")
                );

                DataBase.addUser(joinUser);
                response302Header(dos);
            }
            else if (path.endsWith(".css")) {
                body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
                response200CssHeader(dos, body.length);
            }
            else if (path.equals("/user/login")) {
                if (canLogin(httpRequest)) {
                    responseLoginSuccess(dos);
                }
                else {
                    responseLoginFail(dos);
                }
            }
            else if (path.equals("/user/list")) {
                if (httpRequest.isLogin("logined")) {
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
                body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
                response200Header(dos, body.length);
            }

            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getDefaultPath(String path) {
        if (path.equals("/")) {
            return "/index.html";
        }
        return path;
    }

    private boolean canLogin(HttpRequest httpRequest) {
        User findUser = DataBase.findUserById(httpRequest.getParameter("userId"));

        if (findUser == null) {
            return false;
        }

        if (!findUser.getPassword().equals(httpRequest.getParameter("password"))) {
            return false;
        }

        return true;
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
