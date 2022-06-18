package com.revature.utils;

import com.revature.exceptions.InsufficientFundsException;
import com.revature.exceptions.InvalidFundsException;

public class FundChecker {

    public static void checkFunds(double amt) {
        if(amt < 0) {
            throw new InvalidFundsException(amt);
        }
    }

    public static void checkSufficient(double balance, double amt) {
        if(balance < amt) {
            throw new InsufficientFundsException();
        }
    }
    
}
