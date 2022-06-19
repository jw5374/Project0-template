package com.revature.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        account.addAttachedUser(this);
    }

    public void applyAccount(Bank bank) {
        bank.addPendingAccs(new Account(this));
    }

    public void applyAccount(Bank bank, boolean joint, double balance) {
        bank.addPendingAccs(new Account(joint, balance, this));
    }

}
