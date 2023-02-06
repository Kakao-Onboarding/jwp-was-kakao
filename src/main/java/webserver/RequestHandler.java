package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.handler.Handler;
import webserver.http.*;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final Map<String, Handler> urlHandlerMapping;
    private final List<Handler> defaultHandlerMapping;

    public RequestHandler(Socket connectionSocket, Map<String, Handler> urlHandlerMapping, List<Handler> defaultHandlerMapping) {
        this.connection = connectionSocket;
        this.urlHandlerMapping = urlHandlerMapping;
        this.defaultHandlerMapping = defaultHandlerMapping;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            HttpRequest request = getHttpRequest(br);
            if (request == null) {
                return;
            }

            HttpResponse httpResponse = getHttpResponse(request);

            System.out.println(httpResponse);

            DataOutputStream dos = new DataOutputStream(out);
            dos.write(httpResponse.toBytes());
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse getHttpResponse(HttpRequest request) {
        Handler urlHandler = urlHandlerMapping.get(request.getURL());
        if (urlHandler != null) {
            return urlHandler.handle(request);
        }

        for (Handler handler : defaultHandlerMapping) {
            try {
                return handler.handle(request);
            } catch (Exception ignored) {}
        }

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withVersion("HTTP/1.1")
                .build();
    }

    private static HttpRequest getHttpRequest(BufferedReader br) throws IOException {
        StringBuilder requestStringBuilder = new StringBuilder();

        String line = br.readLine();
        if (line == null) {
            return null;
        }

        requestStringBuilder.append(line).append("\r\n");

        while ((line = br.readLine()) != null && !"".equals(line)) {
            requestStringBuilder.append(line).append("\r\n");
        }
        System.out.println(requestStringBuilder);
        HttpRequest request = new HttpRequestParser().parse(requestStringBuilder.toString());

        if (request.getMethod() == HttpMethod.POST || request.getMethod() == HttpMethod.PUT) {
            String body = IOUtils.readData(br, Integer.parseInt(request.getHeaders().get("Content-Length")));
            System.out.println(body);
            request.setBody(body);
        }
        return request;
    }
}
