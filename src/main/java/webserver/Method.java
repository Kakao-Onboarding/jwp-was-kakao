package webserver;

import java.util.Arrays;

enum Method {
    GET, POST, PUT, DELETE;

    public static Method of(String input) {
        return Arrays.stream(values())
                .filter(method -> method.toString().equalsIgnoreCase(input))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
