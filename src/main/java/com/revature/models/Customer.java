package com.revature.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.revature.utils.MenuPrinter;

public class Customer extends User {

    private List<Account> openAccounts = new ArrayList<>();

    public Customer(String username, String password, String firstname, String lastname, String email, String phone) {
        super(username, password, firstname, lastname, email, phone);
    }

    public void transferFunds(int first, int second, double amt) {
        this.openAccounts.get(first).withdraw(amt);
        this.openAccounts.get(second).deposit(amt);
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

    public void removeOpenAccount(Account account) {
        openAccounts.remove(account);
    }

    public void applyAccount(Bank bank) {
        bank.addPendingAccs(new Account(this));
    }

    public void applyAccount(Bank bank, boolean joint, double balance) {
        bank.addPendingAccs(new Account(joint, balance, this));
    }

    public void printAllAccounts(MenuPrinter mp) throws IOException {
        for(int i = 0; i < openAccounts.size(); i++) {
            mp.printMessage(openAccounts.get(i).toString(i));
            mp.printMessage(openAccounts.get(i).attachedUsersToString());
        }
    }
}
