package com.revature.utils;

import java.util.HashMap;

import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.models.User;

public class UserChecker {

    public static void checkUsername(String username, HashMap<String, User> users) {
        if(users.containsKey(username)) {
            throw new UserAlreadyExistsException(username);
        }
    }

}
