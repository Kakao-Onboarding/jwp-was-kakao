package webserver;

import java.util.Arrays;

public enum FileType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JS("application/javascript", "js"),
    ICO("image/x-icon", "ico"),
    HANDLER("text/html", ""),
    EOT("application/font-eot", "eot"),
    SVG("application/font-svg", "svg"),
    TTF("application/font-ttf", "ttf"),
    WOFF("application/font-woff", "woff"),
    WOFF2("application/font-woff2", "woff2");

    private final String contentType;
    private final String fileExtension;

    FileType(String contentType, String fileExtension) {
        this.contentType = contentType;
        this.fileExtension = fileExtension;
    }

    public static FileType findType(String extension) {
        return Arrays.stream(values())
                .filter(fileType -> fileType.fileExtension.equalsIgnoreCase(extension))
                .findAny()
                .orElse(HANDLER);
    }

    public String getContentType() {
        return contentType;
    }

    public boolean isFont() {
        return this.equals(EOT) || this.equals(SVG) || this.equals(TTF) || this.equals(WOFF) || this.equals(WOFF2);
    }
}
