package webserver;

public class BaseHandler implements Handler {
    @Override
    public byte[] apply(Request request) {
        return "Hello world".getBytes();
    }
}
