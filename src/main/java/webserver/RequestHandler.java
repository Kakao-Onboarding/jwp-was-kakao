package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import utils.FileIoUtils;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (
                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))
             ) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            Request request = Request.parse(reader);

            FileType fileType = request.findRequestedFileType();
            byte[] body = findResponseByPath(request, fileType);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, fileType);
            responseBody(dos, body);
        } catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] findResponseByPath(Request request, FileType fileType) throws IOException, URISyntaxException {
        String path = request.getPath();
        if (fileType == FileType.HTML || fileType == FileType.ICO) {
            return FileIoUtils.loadFileFromClasspath("./templates" + path);
        }
        if (fileType == FileType.CSS || fileType == FileType.JS) {
            return FileIoUtils.loadFileFromClasspath("./static" + path);
        }
        return HandlerMapping.handle(request);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, FileType fileType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + fileType.getContentType() + ";charset=utf-8 \r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + " \r\n");
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
