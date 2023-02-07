package enums;

import java.util.Arrays;

public enum ContentType {

    HTML("text/html", "html"),
    JSON("application/json", "json"),
    CSS("text/css", "css"),

    JS("application/javascript", "js"),
    FONT_WOFF("font/woff", "woff"),
    FONT_WOFF2("font/woff2", "woff2"),
    FONT_EOT("font/eot", "eot"),
    FONT_TTF("font/ttf", "ttf"),
    FONT_SVG("font/svg", "svg"),
    ICON("image/x-icon", "ico"),
    NONE("", "");

    private final String value;
    private final String extensions;

    ContentType(String value, String extensions) {
        this.value = value;
        this.extensions = extensions;
    }

    public static ContentType fromFilename(String filename) {
        return Arrays.stream(ContentType.values())
                .filter(contentType -> contentType.matches(filename))
                .findFirst()
                .orElse(ContentType.NONE);
    }

    public String getValue() {
        return value;
    }

    public String getExtensions() {
        return extensions;
    }

    private boolean matches(String filename) {
        return filename.matches(String.format("^\\S+.(?i)(%s)$", this.extensions));
    }
}
