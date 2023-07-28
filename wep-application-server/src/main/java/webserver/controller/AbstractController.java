package webserver.controller;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

public class AbstractController implements Controller{

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        HttpMethod method = request.getMethod();

        if (method.isGet()) {
            doGet(request, response);
        }

        if (method.isPost()) {
            doPost(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {}

    public void doPost(HttpRequest request, HttpResponse response) {}
}
