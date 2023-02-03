package webserver;

import java.util.Arrays;

import static webserver.Method.*;

public enum HandlerMapping {

    BASE_URL(GET, "/", new BaseHandler());

    private final Method method;
    private final String url;
    private final Handler handler;

    HandlerMapping(Method method, String url, Handler handler) {
        this.method = method;
        this.url = url;
        this.handler = handler;
    }

    private static HandlerMapping findHandler(Request request) {
        Method method = request.getMethod();
        String url = request.getPath();
        return Arrays.stream(values())
                .filter(handlerMapping -> handlerMapping.method == method && handlerMapping.url.equals(url))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static byte[] handle(Request request) {
        HandlerMapping handlerMapping = findHandler(request);
        return handlerMapping.handler.apply(request);
    }
}
