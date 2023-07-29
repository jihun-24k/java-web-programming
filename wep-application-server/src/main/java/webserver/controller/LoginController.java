package webserver.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController extends AbstractController{

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        if (canLogin(request)) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect("/index.html");
        }
        else {
            response.sendRedirect("/login_failed.html");
        }
    }

    private boolean canLogin(HttpRequest request) {
        User findUser = DataBase.findUserById(request.getParameter("userId"));

        if (findUser == null) {
            return false;
        }

        if (!findUser.getPassword().equals(request.getParameter("password"))) {
            return false;
        }

        return true;
    }
}
