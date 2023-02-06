package webserver.handler;

import db.DataBase;
import model.User;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class UserApiHandler implements Handler {
    public HttpResponse handle(HttpRequest httpRequest) {
        User user = null;
        if (httpRequest.getMethod() == HttpMethod.GET) {
            user = parseUserGet(httpRequest);
        } else {
            user = parseUserPost(httpRequest);
        }

        DataBase.addUser(user);

        System.out.println(DataBase.findUserById(user.getUserId()));

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.FOUND)
                .withVersion("HTTP/1.1")
                .withHeaders(Map.of("Location", "/index.html"))
                .build();
    }

    private User parseUserGet(HttpRequest httpRequest) {
        Map<String, String> parameters = httpRequest.getParameters();
        String userId = parameters.get("userId");
        String password = parameters.get("password");
        String name = parameters.get("name");
        String email = parameters.get("email");

        return new User(userId, password, name, email);
    }

    private User parseUserPost(HttpRequest httpRequest) {
        String body = httpRequest.getBody();
        Map<String, String> parameters = parseBody(body);

        String userId = parameters.get("userId");
        String password = parameters.get("password");
        String name = parameters.get("name");
        String email = parameters.get("email");

        return new User(userId, password, name, email);
    }

    private Map<String, String> parseBody(String body){
        String[] splited = body.split("&");
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < splited.length ; i ++){
            String[] keyValue = splited[i].split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            parameters.put(key, value);
        }
        return parameters;
    }

}
