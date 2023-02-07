package model.user;

import db.DataBase;

import java.util.Map;

public class UserService {
    private static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            return new UserService();
        }
        return instance;
    }

    public void save(Map<String, String> userInfo) {
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        DataBase.addUser(user);
    }
}
