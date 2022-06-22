package com.revature.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    
    public UserDoesNotExistException() {
        super("The username you entered does not exist. Please try again.");
    }

}
