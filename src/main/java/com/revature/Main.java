package com.revature;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.dao.AccountOperations;
import com.revature.dao.UserOperations;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.models.*;
import com.revature.services.AccountServices;
import com.revature.services.MenuFunctions;
import com.revature.services.UserServices;
import com.revature.utils.BankSetup;
import com.revature.utils.DatabaseAccess;
import com.revature.utils.MenuPrinter;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);
    
    static Scanner scan = new Scanner(System.in);
    static BufferedOutputStream out = new BufferedOutputStream(System.out);
    static MenuPrinter mp = new MenuPrinter(out);
    static UserServices us = new UserServices(new UserOperations(DatabaseAccess.getConnection()));
    static AccountServices as = new AccountServices(new AccountOperations(DatabaseAccess.getConnection()));
    static String[] userInfo = new String[2];
    static boolean exit = false;
    
    static Bank bank = new BankSetup(DatabaseAccess.getConnection()).setupBank();
    static MenuFunctions mf = new MenuFunctions(bank, mp, scan, as, us);

    static String userChoice;

    public static void main(String[] args) throws IOException {

        logger.info("Logger started...");

        while(!exit) {
            mp.printPreAuthenticated();
            mp.printInputMessage();
            userChoice = scan.next();
            scan.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            handleLogin();
            
            if(userInfo[0] == null) {
                return;
            }

            switch(userInfo[0]) {
                case "Customer":
                    handleUser((Customer) bank.getBankUser(userInfo[1]));
                    break;
                case "Employee":
                    handleUser((Employee) bank.getBankUser(userInfo[1]));
                    break;
                case "Admin":
                    handleUser((Admin) bank.getBankUser(userInfo[1]));
                    break;
            }
        }

        out.close();
        scan.close();
    }

    public static void handleLogin() throws IOException {
        while(!userChoice.equals("q")) {
            switch(userChoice) {
                case "1":
                    try {
                        userInfo = mf.login();
                        logger.info(userInfo[1] + " has successfully logged in");
                    } catch (InvalidCredentialsException e) {
                        logger.warn("An unsuccessful login attempt was made");
                        mp.printMessage(e.getMessage() + "\n");
                    }
                    break;
                case "2":
                    userInfo = mf.register();
                    logger.info(userInfo[1] + " was successfully registered");
                    break;
                default:
                    mp.printMessage("That is not a recognized choice\n");
            }
            if(userInfo[0] != null) {
                return;
            }
            mp.printPreAuthenticated();
            mp.printInputMessage();
            userChoice = scan.next();
        }
        exit = true;
    }

    public static <T> void handleUser(T user) throws IOException {
        mp.printLoggedinMenu(userInfo[0]);
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            switch(userChoice) {
                case "1":
                    accountMenuHandler(user, user.getClass().getSimpleName());
                    break;
                case "2":
                    mp.printMessage(user.toString());
                    mp.printMessage("Press any key to continue\n");

                    scan.nextLine();
                    scan.nextLine();
                    break;
                default:
                    mp.printMessage("That is not a recognized option\n");
            }
            mp.printLoggedinMenu(userInfo[0]);
            mp.printInputMessage();
            userChoice = scan.next();
        }
        userInfo[0] = null;
        userInfo[1] = null;
    }

    public static <T> void accountMenuHandler(T user, String classname) throws IOException {
        switch(classname) {
            case "Customer":
                mf.viewAccounts((Customer) user);
                break;
            case "Employee":
                mf.viewAccounts((Employee) user);
                break;
            case "Admin":
                mf.viewAccounts((Admin) user);
                break;
        }
    }

}
