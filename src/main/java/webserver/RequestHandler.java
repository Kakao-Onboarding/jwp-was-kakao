package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.IOUtils;
import webserver.handler.Handler;
import webserver.http.HttpRequest;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

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

            StringBuilder requestStringBuilder = new StringBuilder();

            String line = br.readLine();
            if (line == null) {
                return;
            }

            requestStringBuilder.append(line).append("\r\n");

            Map<String, List<String>> headers = new LinkedHashMap<>();
            while ((line = br.readLine()) != null && !"".equals(line)) {
                String[] headerAndVaules = line.split(": ");
                String header = headerAndVaules[0];
                List<String> values = new ArrayList<>();
                Arrays.stream(headerAndVaules).skip(1).forEach(values::add);
                headers.put(header, values);
                requestStringBuilder.append(line).append("\r\n");
            }

            if (requestStringBuilder.length() == 0) {
                System.out.println("exit");
                return;
            }

            requestStringBuilder.append("\r\n");

            if (headers.containsKey("Content-Length")) {
                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length").get(0)));
                requestStringBuilder.append(body);
            }

            System.out.println(requestStringBuilder.toString());

            HttpRequest request = new HttpRequestParser().parse(requestStringBuilder.toString());

            HttpResponse httpResponse = null;
            Handler urlHandler = urlHandlerMapping.get(request.getURL());
            if (urlHandler != null) {
                httpResponse = urlHandler.handle(request);
            } else {
                for (Handler handler : defaultHandlerMapping) {
                    try {
                        httpResponse = handler.handle(request);
                        break;
                    } catch (Exception ignored) {}
                }
                if (httpResponse == null) {
                    httpResponse = HttpResponse.HttpResponseBuilder.aHttpResponse()
                            .withStatus(HttpStatus.BAD_REQUEST)
                            .withVersion("HTTP/1.1")
                            .build();
                }
            }

            System.out.println(httpResponse);

            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(httpResponse.toString());
            if(httpResponse.getBody() != null){
                dos.write(httpResponse.getBody());
            }
            dos.flush();

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
