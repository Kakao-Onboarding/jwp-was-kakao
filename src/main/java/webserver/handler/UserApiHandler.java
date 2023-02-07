package webserver.handler;

import webserver.controller.UserController;
import webserver.http.HttpMethod;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.util.Map;

public class UserApiHandler implements Handler {
    UserController userController = new UserController();
    public HttpResponse handle(HttpRequest httpRequest) {
        if (httpRequest.getMethod() == HttpMethod.GET) {
            userController.getCreateUser(httpRequest);
        } else if(httpRequest.getMethod() == HttpMethod.POST){
            userController.postCreateUser(httpRequest);
        }

        return HttpResponse.HttpResponseBuilder.aHttpResponse()
                .withStatus(HttpStatus.FOUND)
                .withVersion("HTTP/1.1")
                .withHeaders(Map.of("Location", "/index.html"))
                .build();
    }

}
