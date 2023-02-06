package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

@FunctionalInterface
public interface Handler {
    Response apply(Request request);
}
