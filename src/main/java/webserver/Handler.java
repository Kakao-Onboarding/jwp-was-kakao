package webserver;

@FunctionalInterface
public interface Handler {
    byte[] apply(Request request);
}
