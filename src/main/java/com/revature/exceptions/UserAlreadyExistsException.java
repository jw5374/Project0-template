package com.revature.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    
    public UserAlreadyExistsException(String username) {
        super("Username must be unique. The username '" + username + "' already exists.");
    }

}
