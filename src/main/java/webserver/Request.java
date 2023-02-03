package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Request {
    private final List<String> requestLines;

    private Request(List<String> requestLines) {
        this.requestLines = requestLines;
    }

    public static Request parse(BufferedReader reader) throws IOException {
        List<String> requestLines = new ArrayList<>();
        String line;
        while (!Objects.equals(line = reader.readLine(), "")) {
            requestLines.add(line);
        }
        return new Request(requestLines);
    }

    public String getPath() {
        String[] firstRequestLine = requestLines.get(0).split(" ");
        return firstRequestLine[1].split("\\?")[0];
    }

    public Map<String, String> getQueryString() {
        String[] firstRequestLine = requestLines.get(0).split(" ");
        String queryString = firstRequestLine[1].split("\\?")[1];

        return Arrays.stream(queryString.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1], (a, b) -> b));
    }

    // TODO: getRequestBody() 구현하기 (returns Map<String, String>)

    public FileType findRequestedFileType() {
        String path = getPath();
        String[] split = path.split("\\.");
        String fileExtension = split[split.length - 1];
        return FileType.findType(fileExtension);
    }

    public Method getMethod() {
        String[] firstRequestLine = requestLines.get(0).split(" ");
        return Method.of(firstRequestLine[0]);
    }
}
