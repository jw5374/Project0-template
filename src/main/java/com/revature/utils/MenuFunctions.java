package com.revature.utils;

import java.util.Scanner;

import com.revature.exceptions.InvalidCredentialsException;
import com.revature.models.Bank;
import com.revature.models.User;

public class MenuFunctions {
    
    public static String[] login(Bank bank,  Scanner scan) {
        String uname, passw;
        User user;
        System.out.println("---------------------------------------");
        System.out.print("Enter your username: ");
        uname = scan.next();
        System.out.print("Enter your password: ");
        passw = scan.next();
        if(!bank.getBankUsers().containsKey(uname)) {
            throw new InvalidCredentialsException();
        }
        user = bank.getBankUser(uname);
        if(!user.getPassword().equals(passw)) {
            throw new InvalidCredentialsException();
        }
        String[] result = { user.getClass().getSimpleName(), uname };
        return result;
    }

    public static String[] register(Bank bank, Scanner scan) {

        String[] result = {};
        return result;
    }

}
