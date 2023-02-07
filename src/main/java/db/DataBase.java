package db;

import model.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("Added " + user.getName());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
