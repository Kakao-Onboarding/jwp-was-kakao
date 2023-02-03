package webserver;

import db.DataBase;
import model.User;

import java.util.Map;

public class CreateUserHandler implements Handler {
    @Override
    public byte[] apply(Request request) {
        // TODO: getRequestBody() 로 변경
        Map<String, String> queryString = request.getQueryString();
        User user = new User(
                queryString.get("userId"),
                queryString.get("password"),
                queryString.get("name"),
                queryString.get("email")
        );
        DataBase.addUser(user);
        return new byte[0];
    }
}
