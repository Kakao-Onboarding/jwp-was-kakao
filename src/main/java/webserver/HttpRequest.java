package webserver;

import lombok.Getter;
import utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpRequest {

    private final HttpMethod method;

    private final String path;

    private final Map<String, String> header;

    private final String body;

    public HttpRequest(BufferedReader bufferedReader) throws IOException {
        String firstLine = bufferedReader.readLine();
        this.method = HttpMethod.valueOf(firstLine.split(" ")[0]);
        this.path = firstLine.split(" ")[1];
        this.header = parseHttpHeaders(bufferedReader);
        this.body = parseHttpBody(bufferedReader);
    }

    private String parseHttpBody(BufferedReader bufferedReader) throws IOException {
        if (!this.header.containsKey("Content-Length")) {
            return "";
        }

        int contentLength = Integer.parseInt(this.header.get("Content-Length"));
        return IOUtils.readData(bufferedReader, contentLength);
    }

    private Map<String, String> parseHttpHeaders(BufferedReader bufferedReader) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = bufferedReader.readLine();
        while (!"".equals(line)) {
            int index = line.indexOf(":");
            header.put(line.substring(0, index), line.substring(index + 1)
                    .trim());
            line = bufferedReader.readLine();
        }

        return header;
    }
}
