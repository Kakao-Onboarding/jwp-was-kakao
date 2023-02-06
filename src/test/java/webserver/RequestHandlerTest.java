package webserver;

import org.junit.jupiter.api.Test;
import support.StubSocket;
import utils.FileIoUtils;
import webserver.handler.Handler;
import webserver.handler.HtmlRequestHandler;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UserApiHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestHandlerTest {

    @Test
    void index() throws IOException, URISyntaxException {
        // given
        final String httpRequest = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        final var socket = new StubSocket(httpRequest);
        final RequestHandler handler = new RequestHandler(socket, initUrlHandlerMapping(), initHandlerMapping());

        // when
        handler.run();

        // then


        var expected = "HTTP/1.1 200 OK \r\n" +
                "Content-Type: text/html;charset=utf-8 \r\n" +
                "Content-Length: 6902 \r\n" +
                "\r\n" +
                new String(FileIoUtils.loadFileFromClasspath("templates/index.html"));

        assertThat(socket.output()).isEqualTo(expected);
    }

    private static List<Handler> initHandlerMapping() {
        List<Handler> handlerMapping = new ArrayList<>();
        handlerMapping.add(new HtmlRequestHandler());
        handlerMapping.add(new StaticResourceRequestHandler());
        return handlerMapping;
    }

    private static Map<String, Handler> initUrlHandlerMapping() {
        HashMap<String, Handler> urlHandlerMapping = new HashMap<>();
        urlHandlerMapping.put("/user/create", new UserApiHandler());
        return urlHandlerMapping;
    }
}