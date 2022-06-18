package com.revature.models;

import com.revature.utils.FundChecker;

public class Account {

    private static int idCounter = 1;
    private int id;
    private boolean isJoint;
    private double balance;

    public Account() {
        this.isJoint = false;
        this.balance = 0.0;
        this.id = idCounter;
        idCounter++;
    }

    public Account(boolean isJoint, double balance) {
        FundChecker.checkFunds(balance);
        this.isJoint = isJoint;
        this.balance = balance;
        this.id = idCounter;
        idCounter++;
    }

    public void withdraw(double amt) {
        FundChecker.checkFunds(amt);
        FundChecker.checkSufficient(this.balance, amt);
        this.balance -= amt;
    }
    
    public void deposit(double amt) {
        FundChecker.checkFunds(amt);
        this.balance += amt;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isJoint() {
        return isJoint;
    }

    public void setJoint(boolean isJoint) {
        this.isJoint = isJoint;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
