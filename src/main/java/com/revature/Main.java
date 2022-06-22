package com.revature;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Scanner;

import com.revature.exceptions.InvalidCredentialsException;
import com.revature.models.*;
import com.revature.utils.MenuPrinter;
import com.revature.utils.MenuFunctions;

public class Main {

    static Scanner scan = new Scanner(System.in);
    static BufferedOutputStream out = new BufferedOutputStream(System.out);
    static MenuPrinter mp = new MenuPrinter(out);
    static String[] userInfo = new String[2];
    static boolean exit = false;

    static Bank bank = new Bank();

    static Customer testUser = new Customer("test1", "password", "John", "Smith", "email@email.com", "1234567890");
    static Employee testEmployee = new Employee("test2", "password", "John", "Wayne", "email2@email.com", "1234567891");

    static String userChoice;

    public static void main(String[] args) throws IOException {
        bank.addBankUser("test1", testUser);
        bank.addBankUser("test2", testEmployee);
        testUser.applyAccount(bank);
        testUser.applyAccount(bank, false, 300);
        testEmployee.approveAcc(bank, 0);
        
        while(!exit) {
            mp.printPreAuthenticated();
            mp.printInputMessage();
            userChoice = scan.next();
            scan.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            handleLogin();
            
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
                        userInfo = MenuFunctions.login(bank, scan);
                    } catch (InvalidCredentialsException e) {
                        mp.printMessage(e.getMessage() + "\n");
                    }
                    break;
                case "2":
                    userInfo = MenuFunctions.register(bank, scan);
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
                MenuFunctions.viewAccounts(bank, (Customer) user, scan, mp);
                break;
            case "Employee":
                MenuFunctions.viewAccounts(bank, (Employee) user, scan, mp);
                break;
            case "Admin":
                MenuFunctions.viewAccounts(bank, (Admin) user, scan, mp);
                break;
        }
    }

}
