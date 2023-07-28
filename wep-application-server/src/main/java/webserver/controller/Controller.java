package webserver.controller;

import http.HttpResponse;

public interface Controller {
    void service(HttpResponse request, HttpResponse response);
}
