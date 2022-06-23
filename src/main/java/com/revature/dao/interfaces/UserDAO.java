package com.revature.dao.interfaces;

import java.util.List;

import com.revature.models.User;

public interface UserDAO {
    
    String insertUser(User user, String type);

    List<User> fetchAllUsers();

}
