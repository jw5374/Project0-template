package com.revature.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bank {

    private List<Account> pendingAccs = new ArrayList<>();
    private List<Account> approvedAccs = new ArrayList<>();
    private List<Customer> bankCustomers = new ArrayList<>();
    private HashMap<String, User> bankUsers = new HashMap<>();

    public Bank() {}
    
    public Bank(List<Customer> bankCustomers, HashMap<String, User> bankUsers) {
        this.bankCustomers = bankCustomers;
        this.bankUsers = bankUsers;
    }

    public Bank(List<Account> pendingAccs, List<Account> approvedAccs, List<Customer> bankCustomers,
            HashMap<String, User> bankUsers) {
        this.pendingAccs = pendingAccs;
        this.approvedAccs = approvedAccs;
        this.bankCustomers = bankCustomers;
        this.bankUsers = bankUsers;
    }

    public List<Account> getPendingAccs() {
        return pendingAccs;
    }

    public Account popPending(int index) {
        return this.pendingAccs.remove(index);
    }

    public void setPendingAccs(List<Account> pendingAccs) {
        this.pendingAccs = pendingAccs;
    }

    public void addPendingAccs(Account acc) {
        this.pendingAccs.add(acc);
    }
    
    public List<Account> getApprovedAccs() {
        return approvedAccs;
    }

    public Account getApprovedAcc(int index) {
        return this.approvedAccs.get(index);
    }

    public Account popApproved(int index) {
        return this.approvedAccs.remove(index);
    }
    
    public void setApprovedAccs(List<Account> approvedAccs) {
        this.approvedAccs = approvedAccs;
    }
    
    public void addApprovedAccs(Account acc) {
        this.approvedAccs.add(acc);
    }

    public HashMap<String, User> getBankUsers() {
        return bankUsers;
    }

    public void setBankUsers(HashMap<String, User> bankUsers) {
        this.bankUsers = bankUsers;
    }

    public void addBankUser(String username, User user) {
        if(user.getClass().getSimpleName().equals("Customer")) {
            this.bankCustomers.add((Customer) user);
        }
        this.bankUsers.put(username, user);
    }

    public List<Customer> getBankCustomers() {
        return bankCustomers;
    }

    public void setBankCustomers(List<Customer> bankCustomers) {
        this.bankCustomers = bankCustomers;
    }
    
}
