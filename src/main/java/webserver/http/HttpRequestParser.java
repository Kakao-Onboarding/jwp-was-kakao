package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private HttpMethod method;
    private String URL;
    private String version;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;


    public HttpRequest parse(String request){
        parseBody(request.trim());

        return HttpRequest.HttpRequestBuilder.aHttpRequest()
                .withMethod(method)
                .withURL(URL)
                .withVersion(version)
                .withParameters(parameters)
                .withHeaders(headers)
                .withBody(body)
                .build();
    }

    private void parseBody(String request){
        String[] splited = request.split("\r\n\r\n");
        if (splited.length > 1){
            this.body = splited[1];
        }
        parseHeader(splited[0].trim());
    }

    private void parseHeader(String headerString){
        String[] splited = headerString.split("\r\n");
        parseStartLine(splited[0]);
        this.headers = new HashMap<>();
        for (int i = 1; i < splited.length ; i ++){
            String[] keyValue = splited[i].split(": ");
            String key = keyValue[0];
            String value = keyValue[1].trim();
            this.headers.put(key, value);
        }
    }

    private void parseStartLine(String startLine){
        String[] splited = startLine.split(" ");
        this.method = HttpMethod.valueOf(splited[0]);
        parseURL(splited[1]);
        this.version = splited[2];
    }

    private void parseURL(String urlString){
        String[] splited = urlString.split("\\?");
        this.URL = splited[0];
        if (splited.length > 1) {
            parseParameter(splited[1]);
        }


    }
    private void parseParameter(String parameterString){
        String[] splited = parameterString.split("&");
        this.parameters = new HashMap<>();
        for (int i = 0; i < splited.length ; i ++){
            String[] keyValue = splited[i].split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            this.parameters.put(key, value);
        }
    }
}
