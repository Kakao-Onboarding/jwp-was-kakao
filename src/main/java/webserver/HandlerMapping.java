package webserver;

import java.util.Arrays;

import static webserver.Method.*;

public enum HandlerMapping {

    BASE_URL(GET, "/", new BaseHandler()),
    CREATE_USER(GET, "/user/create", new CreateUserHandler());

    private final Method method;
    private final String path;
    private final Handler handler;

    HandlerMapping(Method method, String path, Handler handler) {
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    private static HandlerMapping findHandler(Request request) {
        Method method = request.getMethod();
        String path = request.getPath();
        return Arrays.stream(values())
                .filter(handlerMapping -> handlerMapping.method == method && handlerMapping.path.equals(path))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static byte[] handle(Request request) {
        HandlerMapping handlerMapping = findHandler(request);
        return handlerMapping.handler.apply(request);
    }
}
