package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import java.util.Collection;
import model.User;

public class ListUserController extends AbstractController{

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (request.isLogin("logined")) {
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
    }
}
