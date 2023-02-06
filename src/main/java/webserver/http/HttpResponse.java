package webserver.http;

import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private String version;
    private Map<String, String> headers;
    private byte[] body;

    public HttpStatus getStatus() {
        return status;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public static final class HttpResponseBuilder {
        private HttpStatus status;
        private String version;
        private Map<String, String> headers;
        private byte[] body;

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

        public HttpResponseBuilder withBody(byte[] body) {
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

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(version).append(" ").append(status.getCode()).append(" ").append(status.name()).append(" \r\n");
        if (headers!=null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                sb.append(key).append(": ").append(headers.get(key)).append(" \r\n");
            }
        }
        sb.append("\r\n");

        return sb.toString();
    }

    public byte[] toBytes() throws IOException{
        StringBuilder sb = new StringBuilder();
        sb.append(version).append(" ").append(status.getCode()).append(" ").append(status.name()).append(" \r\n");
        if (headers!=null && !headers.isEmpty()) {
            for (String key : headers.keySet()) {
                sb.append(key).append(": ").append(headers.get(key)).append(" \r\n");
            }
        }
        sb.append("\r\n");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(sb.toString().getBytes());
        if(body != null) {
            outputStream.write(body);
        }
        byte[] response = outputStream.toByteArray();
        return response;
    }
}
