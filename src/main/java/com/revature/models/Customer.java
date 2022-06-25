package com.revature.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.services.AccountServices;
import com.revature.utils.MenuPrinter;

public class Customer extends User {
    private static Logger logger = LogManager.getLogger(Customer.class);
    private List<Account> openAccounts = new ArrayList<>();

    // public Customer(String username, String password, String firstname, String lastname, String email, String phone) {
    //     super(username, password, firstname, lastname, email, phone);
    // }

    public Customer(String username, String password, String firstname, String lastname, String email, String phone,
            AccountServices as) {
        super(username, password, firstname, lastname, email, phone, as);
    }

    public void transferFunds(int first, int second, double amt) {
        this.openAccounts.get(first).withdraw(amt);
        this.openAccounts.get(second).deposit(amt);
        logger.info(getUsername() + " has transferred $" + amt + " from Account " + this.openAccounts.get(first).getId() + " to Account " + this.openAccounts.get(second).getId());
    }

    public List<Account> getOpenAccounts() {
        return openAccounts;
    }

    public Account getOpenAccount(int index) {
        return this.openAccounts.get(index);
    }

    public void setOpenAccounts(Account... openAccounts) {
        this.openAccounts = Arrays.asList(openAccounts);
    }

    public void addOpenAccount(Account account) {
        openAccounts.add(account);
    }

    public void removeOpenAccount(int accId) {
        for(int i = 0; i < openAccounts.size(); i++) {
            if(openAccounts.get(i).getId() == accId) {
                openAccounts.remove(i);
            }
        }
    }

    public void applyAccount(Bank bank) {
        Account applying = new Account(this);
        int id = getAs().insertNewAccount(applying);
        applying.setId(id);
        bank.addPendingAccs(applying);
        logger.info(getUsername() +  " has applied for an account with id: " + applying.getId());
    }

    public void applyAccount(Bank bank, boolean joint, double balance) {
        Account applying = new Account(joint, balance, this);
        int id = getAs().insertNewAccount(applying);
        applying.setId(id);
        bank.addPendingAccs(applying);
        logger.info(getUsername() +  " has applied for an account with id: " + applying.getId());
    }

    public void printAllAccounts(MenuPrinter mp) throws IOException {
        for(int i = 0; i < openAccounts.size(); i++) {
            mp.printMessage(openAccounts.get(i).toString(i));
            mp.printMessage(openAccounts.get(i).attachedUsersToString());
        }
    }
}
