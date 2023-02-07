package webserver.controller;

import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import webserver.service.UserService;

import java.util.Map;

public class UserController {

    UserService userService = new UserService();
    public HttpResponse getCreateUser(HttpRequest httpRequest){
        Map<String, String> param;

        param = ParameterMapper.fromRequestParam(httpRequest);
        User user = ParameterMapper.toUser(param);

        userService.create(user);

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.FOUND)
                .withVersion("HTTP/1.1")
                .withHeaders(Map.of("Location", "/index.html"))
                .build();
    }

    public HttpResponse postCreateUser(HttpRequest httpRequest){
        Map<String, String> param;

        param = ParameterMapper.fromRequestBody(httpRequest);
        User user = ParameterMapper.toUser(param);

        userService.create(user);

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.FOUND)
                .withVersion("HTTP/1.1")
                .withHeaders(Map.of("Location", "/index.html"))
                .build();
    }
}
