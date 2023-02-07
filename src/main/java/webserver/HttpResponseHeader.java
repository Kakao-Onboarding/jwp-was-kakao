package webserver;

import enums.ContentType;
import org.springframework.http.HttpStatus;

import java.util.LinkedList;
import java.util.List;

public class HttpResponseHeader {

    private List<String> headers;

    private HttpResponseHeader(List<String> headers) {
        this.headers = headers;
    }

    public static HttpResponseHeader of(HttpStatus status, ContentType contentType, int contentLength) {
        List<String> headers = new LinkedList<>();

        headers.add(String.format("HTTP/1.1 %d %s", status.value(), status.name()));
        headers.add(String.format("Content-Type: %s;charset=utf-8", contentType.getValue()));
        if (contentLength > 0) {
            headers.add(String.format("Content-Length: %s", contentLength));
        }
        return new HttpResponseHeader(headers);
    }

    public static HttpResponseHeader create302FoundHeader(String redirectURI) {
        return new HttpResponseHeader(List.of(
                String.format("HTTP/1.1 302 FOUND"),
                String.format("Location: %s", redirectURI)
        ));
    }

    public List<String> getHeaders() {
        return headers;
    }
}
