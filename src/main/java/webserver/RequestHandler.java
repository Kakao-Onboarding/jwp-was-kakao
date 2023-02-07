package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import utils.FileIoUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(reader);
            HttpRequest httpRequest = new HttpRequest(bufferedReader);
            handleHttpRequest(httpRequest, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            logger.error("No File");
        }
    }

    private void handleHttpRequest(HttpRequest httpRequest, OutputStream out)
            throws IOException, URISyntaxException, NullPointerException {
        DataOutputStream dos = new DataOutputStream(out);

        if (httpRequest.getMethod() == HttpMethod.POST) {
            handlePostMethodHttpRequest(httpRequest, dos);
        }
        if (httpRequest.getMethod() == HttpMethod.GET) {
            handleGetMethodHttpRequest(httpRequest, dos);
        }
    }

    private void handlePostMethodHttpRequest(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        String path = httpRequest.getPath();
        String requestBody = httpRequest.getBody();
        requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
        MultiValueMap<String, String> requestParams = UriComponentsBuilder.fromUriString(path)
                .query(requestBody)
                .build()
                .getQueryParams();

        if (path.equals("/user/create")) {
            addUser(requestParams);
            response302Header(dos, "http://localhost:8080/index.html");
            dos.flush();
        }
    }

    private void handleGetMethodHttpRequest(HttpRequest httpRequest, DataOutputStream dos)
            throws IOException, URISyntaxException {
        String path = httpRequest.getPath();
        byte[] body;
        String contentType;

        try {
            body = getBodyFromPath(path);
            contentType = Files.probeContentType(new File(path).toPath());
        } catch (IOException e) {
            body = "Hello world".getBytes();
            contentType = "text/html;charset=utf-8";
        }

        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    private byte[] getBodyFromPath(String path) throws IOException, URISyntaxException, NullPointerException {
        byte[] body;
        try {
            body = FileIoUtils.loadFileFromClasspath("./templates" + path);
        } catch (NullPointerException e) {
            body = FileIoUtils.loadFileFromClasspath("./static" + path);
        }
        return body;
    }

    private void addUser(MultiValueMap<String, String> requestParams) {
        String userId = requestParams.getFirst("userId");
        String password = requestParams.getFirst("password");
        String name = requestParams.getFirst("name");
        String email = requestParams.getFirst("email");
        User user = User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();
        DataBase.addUser(user);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + " \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
