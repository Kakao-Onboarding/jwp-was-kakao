package webserver.handler;

import utils.FileIoUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class StaticResourceRequestHandler implements Handler {

    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] bytes;
        try {
            bytes = FileIoUtils.loadFileFromClasspath("./static" + httpRequest.getURL());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.OK)
                .withVersion("HTTP/1.1")
                .withHeaders(generateHeaders(httpRequest, bytes))
                .withBody(bytes)
                .build();
    }

    private Map<String, String> generateHeaders(HttpRequest httpRequest, byte[] body) {
        Map<String, String> headers = new LinkedHashMap<>();

        String url = httpRequest.getURL();
        if (url.endsWith(".js")) {
            headers.put("Content-Type", "text/javascript;charset=utf-8");
        } else if (url.endsWith(".css")) {
            headers.put("Content-Type", "text/css;charset=utf-8");
        } else {
            headers.put("Content-Type", "text/html;charset=utf-8");
        }

        headers.put("Content-Length", String.valueOf(body.length));

        return headers;
    }
}
