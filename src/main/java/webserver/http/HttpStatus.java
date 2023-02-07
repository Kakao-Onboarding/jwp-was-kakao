package webserver.http;

public enum HttpStatus {
    OK(200, "OK"),
    FOUND(302, "Found"),

    BAD_REQUEST(400,"Bad Request");

    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}