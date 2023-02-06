package webserver.request;

import utils.IOUtils;
import webserver.FileType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Request {

    private final Method method;
    private final String url;
    private final String protocol;
    private final Map<String, String> requestHeader;
    private final String requestBody;

    private Request(Method method, String url, String protocol, Map<String, String> requestHeader, String requestBody) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.requestHeader = requestHeader;
        this.requestBody = requestBody;
    }

    public static Request parse(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        Method method = parseMethod(firstLine);
        String url = parseUrl(firstLine);
        String protocol = parseProtocol(firstLine);

        Map<String, String> requestHeader = parseHeader(reader);
        String requestBody = parseBody(reader, requestHeader);

        return new Request(method, url, protocol, requestHeader, requestBody);
    }

    private static Method parseMethod(String firstLine) {
        return Method.of(firstLine.split(" ")[0]);
    }

    private static String parseUrl(String firstLine) {
        return firstLine.split(" ")[1];
    }

    private static String parseProtocol(String firstLine) {
        return firstLine.split(" ")[2];
    }

    private static Map<String, String> parseHeader(BufferedReader reader) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String header;
        while (!Objects.equals(header = reader.readLine(), "")) {
            String[] headerInformation = header.split(":");
            requestHeader.put(headerInformation[0].trim(), headerInformation[1].trim());
        }
        return requestHeader;
    }

    private static String parseBody(BufferedReader reader, Map<String, String> requestHeader) throws IOException {
        if (requestHeader.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
            return IOUtils.readData(reader, contentLength);
        }
        return "";
    }

    public String getPath() {
        return url.split("\\?")[0];
    }

    public Map<String, String> getQueryString() {
        String[] splitUrl = url.split("\\?");
        if (splitUrl.length == 1) {
            return new HashMap<>();
        }
        return parseQueryStringFormat(splitUrl[1]);
    }

    private Map<String, String> parseQueryStringFormat(String input) {
        return Arrays.stream(input.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1], (a, b) -> b));
    }

    public Map<String, String> getRequestBody() {
        return parseQueryStringFormat(requestBody);
    }

    public FileType findRequestedFileType() {
        String path = getPath();
        String[] split = path.split("\\.");
        String fileExtension = split[split.length - 1];
        return FileType.findType(fileExtension);
    }

    public Method getMethod() {
        return method;
    }
}
