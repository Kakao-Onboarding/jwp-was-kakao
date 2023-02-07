package controller;

import enums.ContentType;
import org.springframework.http.HttpStatus;
import utils.FileIoUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public class StaticFileController {
    private static StaticFileController instance;

    private StaticFileController() {
    }

    public static StaticFileController getInstance() {
        if (instance == null) {
            return new StaticFileController();
        }
        return instance;
    }

    public HttpResponse staticFileGet(HttpRequest request) throws IOException, URISyntaxException {
        String requestPath = request.getRequestPath();
        ContentType contentType = ContentType.fromFilename(requestPath);

        String resourcePath = FileIoUtils.getResourcePath(requestPath, contentType);
        byte[] body = FileIoUtils.loadFileFromClasspath(resourcePath);

        return HttpResponse.of(HttpStatus.OK, contentType, body);

    }
}
