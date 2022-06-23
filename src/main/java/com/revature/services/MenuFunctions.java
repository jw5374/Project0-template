package com.revature.services;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.exceptions.InsufficientFundsException;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidFundsException;
import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.exceptions.UserDoesNotExistException;
import com.revature.models.*;
import com.revature.utils.MenuPrinter;

public class MenuFunctions {

    private static Logger logger = LogManager.getLogger(MenuFunctions.class);

    private Bank bank;
    private MenuPrinter mp;
    private Scanner scan;

    public MenuFunctions(Bank bank, MenuPrinter mp, Scanner scan) {
        this.bank = bank;
        this.mp = mp;
        this.scan = scan;
    }

    public String[] login() {
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
    
    public String[] register() {
        String typename, uname, passw, firstn, lastn, email, phone;
        User user;
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
                    user = new Customer(uname, passw, firstn, lastn, email, phone);
                    UserServices.insertNewUser(user);
                    bank.addBankUser(uname, user);
                    result[0] = "Customer";
                    break;
                case "employee":
                    user = new Employee(uname, passw, firstn, lastn, email, phone);
                    UserServices.insertNewUser(user);
                    bank.addBankUser(uname, user);
                    result[0] = "Employee";
                    break;
                case "admin":
                    user = new Admin(uname, passw, firstn, lastn, email, phone);
                    UserServices.insertNewUser(user);
                    bank.addBankUser(uname, user);
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

    public void viewAccounts(Customer user) throws IOException {
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
                        logger.info(user.getUsername() + " has applied for an account with joint status: " + jointstatus + " and balance of $" + amt);
                        break;
                    case "2":
                        for(int i = 0; i < pendings.size(); i++) {
                            if(pendings.get(i).getAttachedUsernames().contains(user.getUsername())) {
                                mp.printMessage(pendings.get(i).toString());
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
                            AccountServices.updateAccount(userAccs.get(index));
                            logger.info(user.getUsername() + " has made Account " + userAccs.get(index).getId() + " a joint account");
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
                                Customer custCast;
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
                                custCast = (Customer) cust;
                                userAccs.get(index).addAttachedUser(custCast);
                                custCast.addOpenAccount(userAccs.get(index));
                                AccountServices.updateAccount(userAccs.get(index));
                                logger.info(user.getUsername() + " has added " + cust.getUsername() +" to Account " + userAccs.get(index).getId());
                                mp.printMessage("Successfully added " + cust.getUsername() + "\n");
                                break;
                            } catch (UserDoesNotExistException e) {
                                logger.warn("UserDoesNotExistException occurred when adding user: " + subChoice + " to joint account");
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
                                AccountServices.updateAccount(userAccs.get(index));
                                logger.info(user.getUsername() + " withdrew $" + amt + " from Account " + userAccs.get(index).getId());
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                mp.printMessage("Invalid index, please enter again: ");
                                index = scan.nextInt();
                            } catch (InsufficientFundsException e) {
                                logger.warn(user.getUsername() + " tried to withdraw too much from Account " + userAccs.get(index).getId());
                                mp.printMessage(e.getMessage() + " Please try again: ");
                                amt = scan.nextDouble();
                            } catch (InvalidFundsException e) {
                                logger.warn(user.getUsername() + " tried to withdraw a negative amount from Account " + userAccs.get(index).getId());
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
                                AccountServices.updateAccount(userAccs.get(index));
                                logger.info(user.getUsername() + " deposited $" + amt + " to Account " + userAccs.get(index).getId());
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                mp.printMessage("Invalid index, please enter again: ");
                                index = scan.nextInt();
                            }  catch (InvalidFundsException e) {
                                logger.warn(user.getUsername() + " tried to deposit a negative amount to Account " + userAccs.get(index).getId());
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
                                AccountServices.updateAccount(userAccs.get(index));
                                AccountServices.updateAccount(userAccs.get(index2));
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

    public void viewAccounts(Employee user) throws IOException {
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
                        approvalMenu(user);
                        break;
                    case "2":
                        customerDetails(subChoice);
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

    public void viewAccounts(Admin user) throws IOException {
        String userChoice;
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
                        approvalMenu(user);
                        break;
                    case "2":
                        bank.printApprovedAccs(mp);
                        mp.printAppovedAccountMenu();
                        approvedAccounts(user);
                        break;
                    case "3":
                        customerDetails(subChoice);
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

    public void customerDetails(String subChoice) throws IOException {
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

                mp.printMessage(selected.toString());
                mp.printMessage("Would you like to view their account details (y/n)? ");
                subChoice = scan.next();
                if(subChoice.equals("y")) {
                    Customer cust = (Customer) selected;
                    for(Account a : cust.getOpenAccounts()) {
                        mp.printMessage(a.toString());
                    }
                    mp.printMessage("Enter any key to continue ");
                    scan.nextLine();
                    scan.nextLine();
                }
                mp.printMessage("Customer usernames:\n");
                for(Customer cust : bank.getBankCustomers()) {
                    mp.printMessage(cust.getUsername() + "\n");
                }
                mp.printMessage("Enter username of customer for details (or q to exit): ");
                subChoice = scan.next();
            } catch (UserDoesNotExistException e) {
                mp.printMessage(e.getMessage() + "\n");
                subChoice = scan.next();
                continue;
            }
        }
    }

    public void approvalMenu(Employee user) throws IOException {
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

    public void approvedAccounts(Admin user) throws IOException {
        String userChoice;
        Account acc;
        mp.printInputMessage();
        userChoice = scan.next();
        while(!userChoice.equals("q")) {
            int index, index2;
            double amt;
            try {
                switch(userChoice) {
                    case "1":
                        mp.printMessage("Enter the index of the account you would like to cancel: ");
                        index = scan.nextInt();
                        user.cancelAccount(bank, index);
                        break;
                    case "2":
                        mp.printMessage("Enter the index of the account you would like to withdraw from: ");
                        index = scan.nextInt();
                        acc = bank.getApprovedAcc(index);
                        mp.printMessage("Enter the amount you would like to withdraw: ");
                        amt = scan.nextDouble();
                        acc.withdraw(amt);
                        AccountServices.updateAccount(acc);
                        logger.info("Admin: " + user.getUsername() + " has withdrawn $" + amt + " from Account " + acc.getId());
                        break;
                    case "3":
                        mp.printMessage("Enter the index of the account you would like to deposit to: ");
                        index = scan.nextInt();
                        acc = bank.getApprovedAcc(index);
                        mp.printMessage("Enter the amount you would like to deposit: ");
                        amt = scan.nextDouble();
                        acc.deposit(amt);
                        AccountServices.updateAccount(acc);
                        logger.info("Admin: " + user.getUsername() + " has deposited $" + amt + " to Account " + acc.getId());
                        break;
                    case "4":
                        mp.printMessage("Enter the index of the account you would like to transfer from: ");
                        index = scan.nextInt();
                        mp.printMessage("Enter the index of the account you would like to transfer to: ");
                        index2 = scan.nextInt();
                        mp.printMessage("Enter the amount you would like to transfer: ");
                        amt = scan.nextDouble();
                        user.transferFunds(bank.getApprovedAcc(index), bank.getApprovedAcc(index2), amt);
                        acc = bank.getApprovedAcc(index);
                        AccountServices.updateAccount(acc);
                        acc = bank.getApprovedAcc(index2);
                        AccountServices.updateAccount(acc);
                        break;
                    default:
                        mp.printMessage("That is not a recognized choice\n");
                }
            } catch (InputMismatchException e) {
                mp.printMessage(e.getClass().getSimpleName() + " please try again");
                scan.nextLine();
                scan.nextLine();
            } catch (IndexOutOfBoundsException e) {
                mp.printMessage(e.getClass().getSimpleName() + " please try again");
                scan.nextLine();
                scan.nextLine();
            } catch (InvalidFundsException e) {
                mp.printMessage(e.getMessage());
                scan.nextLine();
                scan.nextLine();
            } catch (InsufficientFundsException e) {
                mp.printMessage(e.getMessage());
                scan.nextLine();
                scan.nextLine();
            }
            
            bank.printApprovedAccs(mp);
            mp.printAppovedAccountMenu();
            mp.printInputMessage();
            userChoice = scan.next();
        }
    }
}
