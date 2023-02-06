package webserver;

@FunctionalInterface
public interface Handler {
    Response apply(Request request);
}
