package webserver;

import java.util.Arrays;

public enum FileType {
    HTML("text/html", "html"),
    CSS("text/css", "css"),
    JS("application/javascript", "js"),
    ICO("image/x-icon", "ico"),
    OTHER("text/html", "");

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
                .orElse(OTHER);
    }

    public String getContentType() {
        return contentType;
    }
}
