package webserver.controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class CreateUserController extends AbstractController{
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User joinUser = new User(
            request.getParameter("userId")
            ,request.getParameter("password")
            ,request.getParameter("name")
            ,request.getParameter("email")
        );

        DataBase.addUser(joinUser);
        response.sendRedirect("/index.html");
    }
}
