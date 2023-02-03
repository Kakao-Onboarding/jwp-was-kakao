package webserver.http;

import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private String version;
    private Map<String, String> headers;
    private String body;

    public HttpStatus getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public static final class HttpResponseBuilder {
        private HttpStatus status;
        private String version;
        private Map<String, String> headers;
        private String body;

        private HttpResponseBuilder() {
        }

        public static HttpResponseBuilder aHttpResponse() {
            return new HttpResponseBuilder();
        }

        public HttpResponseBuilder withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public HttpResponseBuilder withVersion(String version) {
            this.version = version;
            return this;
        }

        public HttpResponseBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpResponseBuilder withBody(String body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.version = this.version;
            httpResponse.headers = this.headers;
            httpResponse.body = this.body;
            httpResponse.status = this.status;
            return httpResponse;
        }
    }
}
