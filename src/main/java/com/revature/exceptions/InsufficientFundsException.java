package com.revature.exceptions;

public class InsufficientFundsException extends RuntimeException {
    
    public InsufficientFundsException() {
        super("You have insufficient funds to perform that action");
    }

}
