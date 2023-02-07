package utils;

import exceptions.InvalidQueryParameterException;
import webserver.HttpRequest;
import webserver.HttpRequestHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IOUtils {
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    // HttpReqeust
    // header, body

    public static HttpRequest parseReqeust(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        HttpRequestHeader header = extractRequestHeader(bufferedReader);
        String body = IOUtils.readData(bufferedReader, header.getContentLength().orElse(0));
        return new HttpRequest(header, body);
    }

    public static HttpRequestHeader extractRequestHeader(BufferedReader bufferedReader) {
        try {
            List<String> headers = new ArrayList<>();
            String line;
            while (!"".equals(line = bufferedReader.readLine())) {
                headers.add(line);
                if (line == null) break;
            }
            return new HttpRequestHeader(headers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static Map<String, String> extractUserFromPath(String path) {
        String[] token = path.split("\\?");
        try {
            return extractUser(token[1]);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidQueryParameterException();
        }
    }

    public static Map<String, String> extractUser(String params) throws IndexOutOfBoundsException {
        String[] queryParams = params.split("&");
        return Arrays.stream(queryParams).map(s -> s.split("="))
                .collect(Collectors.toMap(
                        a -> a[0],  //key
                        a -> URLDecoder.decode(a[1])   //value
                ));
    }
}
