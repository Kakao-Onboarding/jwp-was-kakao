package webserver;

import java.util.List;
import java.util.Optional;

public class HttpRequestHeader {
    private List<String> headers;

    public HttpRequestHeader(List<String> headers) {
        this.headers = headers;
    }

    public String getRequestMethod() {
        String[] token = headers.get(0).split(" ");
        return token[0];
    }

    public String getRequestPath() {
        String[] token = headers.get(0).split(" ");
        return token[1];
    }

    public Optional<Integer> getContentLength() {
        return headers.stream()
                .filter(line -> line.startsWith("Content-Length: "))
                .findFirst()
                .map(line -> line.substring(16))
                .map(String::trim)
                .map(Integer::parseInt);
    }
}
