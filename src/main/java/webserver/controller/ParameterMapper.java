package webserver.controller;

import model.User;
import webserver.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class ParameterMapper {
    public static Map<String, String> fromRequestParam(HttpRequest httpRequest) {
        Map<String, String> parameters = httpRequest.getParameters();

        return parameters;
    }

    public static Map<String, String> fromRequestBody(HttpRequest httpRequest) {
        String body = httpRequest.getBody();
        Map<String, String> parameters = parseBody(body);

        return parameters;
    }

    public static User toUser(Map<String, String> parameters){
        String userId = parameters.get("userId");
        String password = parameters.get("password");
        String name = parameters.get("name");
        String email = parameters.get("email");

        return new User(userId, password, name, email);
    }

    private static Map<String, String> parseBody(String body){
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
