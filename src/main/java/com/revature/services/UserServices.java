package com.revature.services;

import java.util.List;

import com.revature.dao.UserOperations;
import com.revature.dao.interfaces.UserDAO;
import com.revature.models.User;

public class UserServices {
    private static UserDAO uo = new UserOperations();

    public static String insertNewUser(User user) {
        return uo.insertUser(user, user.getClass().getSimpleName());
    }
    
    public static List<User> retrieveAllUsers() {
        return uo.fetchAllUsers();
    }

}
