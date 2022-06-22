package com.revature.utils;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.revature.exceptions.InsufficientFundsException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidFundsException;
import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.exceptions.UserDoesNotExistException;
import com.revature.models.Account;
import com.revature.models.Admin;
import com.revature.models.Bank;
import com.revature.models.Customer;
import com.revature.models.Employee;
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
        String typename, uname, passw, firstn, lastn, email, phone;
        String[] result = new String[2];
        System.out.print("Please enter username: ");
        uname = scan.next();
        while(bank.getBankUsers().containsKey(uname)) {
            System.out.println(new UserAlreadyExistsException(uname).getMessage());
            System.out.print("Please enter register Username: ");
            uname = scan.next();
        }
        System.out.print("Please enter Password: ");
        passw = scan.next();
        System.out.print("Please enter your First Name: ");
        firstn = scan.next();
        System.out.print("Please enter your Last Name: ");
        lastn = scan.next();
        System.out.print("Please enter your Email: ");
        email = scan.next();
        System.out.print("Please enter your Phone Number: ");
        phone = scan.next();
        System.out.print("Please enter the account type you're registering for ('Customer', 'Employee', 'Admin'): ");
        typename = scan.next().toLowerCase();
        while(true) {
            switch(typename) {
                case "customer":
                    bank.addBankUser(uname, new Customer(uname, passw, firstn, lastn, email, phone));
                    result[0] = "Customer";
                    break;
                case "employee":
                    bank.addBankUser(uname, new Employee(uname, passw, firstn, lastn, email, phone));
                    result[0] = "Employee";
                    break;
                case "admin":
                    bank.addBankUser(uname, new Admin(uname, passw, firstn, lastn, email, phone));
                    result[0] = "Admin";
                    break;
                default:
                    System.out.println("That is not a valid account type: " + typename);
                    typename = scan.next().toLowerCase();
                    continue;
            }
            break;
        }

        result[1] = uname;
        return result;
    }

    public static void viewAccounts(Bank bank, Customer user, Scanner scan, MenuPrinter mp) throws IOException {
        String userChoice;
        List<Account> userAccs = user.getOpenAccounts();
        List<Account> pendings = bank.getPendingAccs();
        user.printAllAccounts(mp);
        mp.printAccountMenuCustomer();
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            double amt;
            boolean jointstatus;
            int index, index2;
            String subChoice = "";
            try {
                switch(userChoice) {
                    case "1":
                        mp.printMessage("Would you like a joint account [y/n] (You may add individuals after approval)? ");
                        if(!scan.next().equals("y")) {
                            jointstatus = false;   
                        } else {
                            jointstatus = true;
                        }
                        mp.printMessage("Enter the initial balance you would like (0 if nothing): ");
                        amt = scan.nextDouble();
                        user.applyAccount(bank, jointstatus, amt);
                        break;
                    case "2":
                        for(int i = 0; i < pendings.size(); i++) {
                            if(pendings.get(i).getAttachedUsers().contains(user)) {
                                mp.printMessage(pendings.get(i).toString(i));
                            }
                        }
                        mp.printMessage("Press any key to return to account menu ");
                        scan.nextLine();
                        scan.nextLine();
                        break;
                    case "3":
                        mp.printMessage("Enter the account index you would like to add users to/make joint: ");
                        index = scan.nextInt();
                        if(!userAccs.get(index).isJoint()) {
                            userAccs.get(index).setJoint(true);
                            mp.printMessage("This account is now a joint account\n");
                            mp.printMessage("Would you like to add users to this account (y/n)? ");
                            if(!scan.next().equals("y")) {
                                break;
                            }
                        } else {
                            mp.printMessage("That account is already a joint account\n");
                        }
                        mp.printMessage("Enter the username of the person you would like to add (or q to exit): ");subChoice = scan.next();
                        while(!subChoice.equals("q")) {
                            try {
                                User cust = bank.getBankUser(subChoice);
                                if(!cust.getClass().getSimpleName().equals("Customer")) {
                                    mp.printMessage("That username is not associated with a customer, try again: ");
                                    subChoice = scan.next();
                                    continue;
                                }
                                if(cust.getUsername() == user.getUsername()) {
                                    mp.printMessage("You cannot add yourself, try again: ");
                                    subChoice = scan.next();
                                    continue;
                                }
                                userAccs.get(index).addAttachedUser((Customer) cust);
                                mp.printMessage("Successfully added " + cust.getUsername() + "\n");
                                break;
                            } catch (UserDoesNotExistException e) {
                                mp.printMessage(e.getMessage() + "\n");
                                subChoice = scan.next();
                                continue;
                            }
                        }
    
                        break;
                    case "4":
                        mp.printMessage("Enter the index of the account you would like to withdraw from: ");
                        index = scan.nextInt();
                        mp.printMessage("Enter the amount you would like to withdraw: ");
                        amt = scan.nextDouble();
                        while(true) {
                            try {
                                userAccs.get(index).withdraw(amt);
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                mp.printMessage("Invalid index, please enter again: ");
                                index = scan.nextInt();
                            } catch (InsufficientFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            } catch (InvalidFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            }
                        }
                        mp.printMessage("Successfully withdrawn $" + amt + "\n");
                        break;
                    case "5":
                        mp.printMessage("Enter the index of the account you would like to deposit to: ");
                        index = scan.nextInt();
                        mp.printMessage("Enter the amount you would like to deposit: ");
                        amt = scan.nextDouble();
                        while(true) {
                            try {
                                userAccs.get(index).deposit(amt);
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                mp.printMessage("Invalid index, please enter again: ");
                                index = scan.nextInt();
                            } catch (InsufficientFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            } catch (InvalidFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            }
                        }
                        mp.printMessage("Successfully deposited $" + amt + "\n");
                        break;
                    case "6":
                        mp.printMessage("Enter the index of the account you would like to transfer from: ");
                        index = scan.nextInt();
                        mp.printMessage("Enter the index of the account you would like to transfer to: ");
                        index2 = scan.nextInt();
                        mp.printMessage("Enter the amount you would like to transfer: ");
                        amt = scan.nextDouble();
                        while(true) {
                            try {
                                user.transferFunds(index, index2, amt);
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                mp.printMessage("Invalid index, please enter again:\n");
                                mp.printMessage("Index of the account to trasnfer from: ");
                                index = scan.nextInt();
                                mp.printMessage("Index of the account to transfer to: ");
                                index2 = scan.nextInt();
                            } catch (InsufficientFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            } catch (InvalidFundsException e) {
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            }
                        }
                        mp.printMessage("Successfully transferred $" + amt + "\n");
                        break;
                    default:
                        mp.printMessage("That is not a recognized choice\n");
                }
            } catch (Exception e) {
                mp.printMessage(e.getClass().getSimpleName() + ": " + (e.getMessage() == null ? "No message" : e.getMessage()));
                scan.nextLine();
                scan.nextLine();
            }
            user.printAllAccounts(mp);
            mp.printAccountMenuCustomer();
            mp.printInputMessage();
            userChoice = scan.next();
        }
    }

    public static void viewAccounts(Bank bank, Employee user, Scanner scan, MenuPrinter mp) throws IOException {
        String userChoice;
        mp.printAccountMenuEmployee();
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            String subChoice = "";
            try {
                switch(userChoice) {
                    case "1":
                        bank.printPendingAccs(mp);
                        mp.printApprovalMenu();
                        approvalMenu(mp, scan, bank, user);
                        break;
                    case "2":
                        customerDetails(mp, scan, subChoice, bank);
                        break;
                    default:
                        mp.printMessage("That is not a recognized choice\n");
                }
            } catch (Exception e) {
                mp.printMessage(e.getClass().getSimpleName() + ": " + (e.getMessage() == null ? "No message" : e.getMessage()));
                scan.nextLine();
                scan.nextLine();
            }
            mp.printAccountMenuEmployee();
            mp.printInputMessage();
            userChoice = scan.next();
        }
    }

    public static void viewAccounts(Bank bank, Admin user, Scanner scan, MenuPrinter mp) throws IOException {
        String userChoice;
        // List<Account> pendings = bank.getPendingAccs();
        mp.printAccountMenuAdmin();
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            String subChoice = "";
            try {
                switch(userChoice) {
                    case "1":
                        bank.printPendingAccs(mp);
                        mp.printApprovalMenu();
                        approvalMenu(mp, scan, bank, user);
                        break;
                    case "2":
                        break;
                    case "3":
                        customerDetails(mp, scan, subChoice, bank);
                        break;
                    default:
                        mp.printMessage("That is not a recognized choice\n");
                }
            } catch (Exception e) {
                mp.printMessage(e.getClass().getSimpleName() + ": " + (e.getMessage() == null ? "No message" : e.getMessage()));
                scan.nextLine();
                scan.nextLine();
            }
            mp.printAccountMenuAdmin();
            mp.printInputMessage();
            userChoice = scan.next();
        }
    }

    public static void customerDetails(MenuPrinter mp, Scanner scan, String subChoice, Bank bank) throws IOException {
        mp.printMessage("Customer usernames:\n");
        for(Customer cust : bank.getBankCustomers()) {
            mp.printMessage(cust.getUsername() + "\n");
        }
        mp.printMessage("Enter username of customer for details (or q to exit): ");
        subChoice = scan.next();
        while(!subChoice.equals("q")) {
            try {
                User selected = bank.getBankUser(subChoice);
                if(!selected.getClass().getSimpleName().equals("Customer")) {
                    mp.printMessage("That is not a customer name try again\n");
                    subChoice = scan.next();
                    continue;
                }
                mp.printMessage("Customer usernames:\n");
                for(Customer cust : bank.getBankCustomers()) {
                    mp.printMessage(cust.getUsername() + "\n");
                }
                mp.printMessage(selected.toString());
                mp.printMessage("Enter username of customer for details (or q to exit): ");
                subChoice = scan.next();
            } catch (UserDoesNotExistException e) {
                mp.printMessage(e.getMessage() + "\n");
                subChoice = scan.next();
                continue;
            }
        }
    }

    public static void approvalMenu(MenuPrinter mp, Scanner scan, Bank bank, Employee user) throws IOException {
        String userChoice;
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            int accIndex;
            switch(userChoice) {
                case "1":
                    while(true) {
                        try {
                            mp.printMessage("Enter the index of the account you would like to approve: ");
                            accIndex = scan.nextInt();
                            user.approveAcc(bank, accIndex);
                            break;
                        } catch (InputMismatchException e) {
                            mp.printMessage(e.getClass().getSimpleName() + " please try again");
                            scan.nextLine();
                            scan.nextLine();
                        } catch (IndexOutOfBoundsException e) {
                            mp.printMessage(e.getClass().getSimpleName() + " please try again");
                            scan.nextLine();
                            scan.nextLine();
                        }
                    }
                    break;
                case "2":
                    while(true) {
                        try {
                            mp.printMessage("Enter the index of the account you would like to deny: ");
                            accIndex = scan.nextInt();
                            user.denyAcc(bank, accIndex);
                            break;
                        } catch (InputMismatchException e) {
                            mp.printMessage(e.getClass().getSimpleName() + " please try again");
                            scan.nextLine();
                            scan.nextLine();
                        } catch (IndexOutOfBoundsException e) {
                            mp.printMessage(e.getClass().getSimpleName() + " please try again");
                            scan.nextLine();
                            scan.nextLine();
                        }
                    }
                    break;
                default:
                    mp.printMessage("That is not a recognized choice\n");
            }
            bank.printPendingAccs(mp);
            mp.printApprovalMenu();
            mp.printInputMessage();
            userChoice = scan.next();
        }
    }
}
