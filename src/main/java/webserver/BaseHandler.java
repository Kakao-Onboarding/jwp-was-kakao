package webserver;

public class BaseHandler implements Handler {
    @Override
    public Response apply(Request request) {
        return Response.ok("Hello world".getBytes(), request.findRequestedFileType());
    }
}
