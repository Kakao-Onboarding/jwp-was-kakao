package webserver;

public class HttpRequest {
    private HttpRequestHeader header;
    private String body;

    public HttpRequest(HttpRequestHeader header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getRequestPath() {
        return header.getRequestPath();
    }

    public String getRequestMethod() {
        return header.getRequestMethod();
    }

    public String getBody() {
        return body;
    }
}
