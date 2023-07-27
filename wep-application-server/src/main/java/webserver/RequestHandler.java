package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
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

            HttpResponse response = new HttpResponse(out);

            if (path.equals("/user/create")) {
                User joinUser = new User(
                    httpRequest.getParameter("userId")
                    ,httpRequest.getParameter("password")
                    ,httpRequest.getParameter("name")
                    ,httpRequest.getParameter("email")
                );

                DataBase.addUser(joinUser);
                response.sendRedirect("/index.html");
            }
            else if (path.equals("/user/login")) {
                if (canLogin(httpRequest)) {
                    response.addHeader("Set-Cookie", "logined=true");
                    response.sendRedirect("/index.html");
                }
                else {
                    response.sendRedirect("/login_failed.html");
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
                    response.forwardBody(sb.toString());
                }
                else {
                    response.sendRedirect("/user/login.html");
                }
            }
            else {
                response.forward(path);
            }
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
}
