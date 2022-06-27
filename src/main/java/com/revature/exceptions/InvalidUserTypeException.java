package com.revature.exceptions;

public class InvalidUserTypeException extends RuntimeException {

    public InvalidUserTypeException() {
        super("That is not a valid user type.");
    }
    
}
