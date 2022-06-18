package com.revature.exceptions;

public class InvalidFundsException extends RuntimeException {
    public InvalidFundsException(double funds) {
        super(funds + " is an invalid amount. Please enter a positive number");
    }
}
