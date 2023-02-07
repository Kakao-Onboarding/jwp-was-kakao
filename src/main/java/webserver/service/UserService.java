package webserver.service;

import db.DataBase;
import model.User;

public class UserService {
    public void create(User user){
        DataBase.addUser(user);
    }
}
