package webserver.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class HttpRequestParserTest {

    @Test
    @DisplayName("메세지 바디가 없는 http request를 파싱해서 HttpRequest 객체를 반환한다")
    void parse() {
        final String request = String.join("\r\n",
                "GET /index.html HTTP/1.1 ",
                "Host: localhost:8080 ",
                "Connection: keep-alive ",
                "",
                "");

        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpRequest httpRequest = httpRequestParser.parse(request);

        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(httpRequest.getURL()).isEqualTo("/index.html");
        assertThat(httpRequest.getVersion()).isEqualTo("HTTP/1.1");
        assertThat(httpRequest.getHeaders())
                .containsEntry("Host", "localhost:8080");
        assertThat(httpRequest.getHeaders())
                .containsEntry("Connection", "keep-alive");
        assertThat(httpRequest.getBody()).isNull();
    }

}