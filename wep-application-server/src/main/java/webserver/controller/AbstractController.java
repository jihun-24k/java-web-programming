package webserver.controller;

import http.HttpResponse;

public class AbstractController implements Controller{

    @Override
    public void service(HttpResponse request, HttpResponse response) {

    }

    public void doGet(HttpResponse request, HttpResponse response) {

    }

    public void doPost(HttpResponse request, HttpResponse response) {}
}
