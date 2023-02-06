package webserver;

import db.DataBase;
import model.User;

import java.util.Map;

public class CreateUserHandler implements Handler {
    @Override
    public Response apply(Request request) {
        Map<String, String> queryString = request.getRequestBody();
        User user = new User(
                queryString.get("userId"),
                queryString.get("password"),
                queryString.get("name"),
                queryString.get("email")
        );
        DataBase.addUser(user);
        return Response.found(new byte[0], request.findRequestedFileType(), "/index.html");
    }
}
