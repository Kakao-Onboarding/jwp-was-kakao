package webserver.http;

import java.util.Map;

public class HttpRequest {
    private HttpMethod method;
    private String URL;
    private String version;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;

    private HttpRequest() {}

    public HttpMethod getMethod() {
        return method;
    }

    public String getURL() {
        return URL;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static final class HttpRequestBuilder {
        private HttpMethod method;
        private String URL;
        private String version;
        private Map<String, String> parameters;
        private Map<String, String> headers;
        private String body;


        private HttpRequestBuilder() {
        }

        public static HttpRequestBuilder aHttpRequest() {
            return new HttpRequestBuilder();
        }

        public HttpRequestBuilder withMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder withURL(String URL) {
            this.URL = URL;
            return this;
        }

        public HttpRequestBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public HttpRequestBuilder withParameters(Map<String, String> parameters) {
            this.parameters = parameters;
            return this;
        }

        public HttpRequestBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.parameters = this.parameters;
            httpRequest.body = this.body;
            httpRequest.version = this.version;
            httpRequest.method = this.method;
            httpRequest.URL = this.URL;
            httpRequest.headers = this.headers;
            return httpRequest;
        }
    }
}
