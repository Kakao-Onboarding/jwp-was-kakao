package webserver.handler;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public interface Handler {
    public HttpResponse handle(HttpRequest httpRequest);
}
