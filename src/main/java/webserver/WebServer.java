package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import webserver.handler.Handler;
import webserver.handler.HtmlRequestHandler;
import webserver.handler.StaticResourceRequestHandler;
import webserver.handler.UserApiHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        List<Handler> defaultHandlerMapping = initDefaultHandlerMapping();
        Map<String, Handler> urlHandlerMapping = initUrlHandlerMapping();

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                Thread thread = new Thread(new RequestHandler(connection, urlHandlerMapping, defaultHandlerMapping));
                thread.start();
            }
        }
    }

    private static Map<String, Handler> initUrlHandlerMapping() {
        HashMap<String, Handler> urlHandlerMapping = new HashMap<>();
        urlHandlerMapping.put("/user/create", new UserApiHandler());
        return urlHandlerMapping;
    }

    private static List<Handler> initDefaultHandlerMapping() {
        List<Handler> handlerMapping = new ArrayList<>();
        handlerMapping.add(new HtmlRequestHandler());
        handlerMapping.add(new StaticResourceRequestHandler());
        return handlerMapping;
    }
}
