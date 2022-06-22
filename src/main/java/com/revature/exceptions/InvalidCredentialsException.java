package com.revature.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException() {
        super("The credentials entered are not valid. Please try again.");
    }

}
