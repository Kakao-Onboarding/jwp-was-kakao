package webserver.handler;

import webserver.request.Request;
import webserver.response.Response;

public class BaseHandler implements Handler {
    @Override
    public Response apply(Request request) {
        return Response.ok("Hello world".getBytes(), request.findRequestedFileType());
    }
}
