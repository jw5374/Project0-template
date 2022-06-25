package com.revature.services;

import java.util.List;

import com.revature.dao.interfaces.UserDAO;
import com.revature.models.User;

public class UserServices {
    private UserDAO uo;

    public UserServices(UserDAO uo) {
        this.uo = uo;
    }

    public String insertNewUser(User user) {
        return uo.insertUser(user, user.getClass().getSimpleName());
    }
    
    public List<User> retrieveAllUsers() {
        return uo.fetchAllUsers();
    }

    public void removeUser(String username) {
        uo.deleteUser(username);
    }
}
