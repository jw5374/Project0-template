package com.revature.models;

import java.util.ArrayList;
import java.util.List;

import com.revature.utils.FundChecker;

public class Account {

    public static int idCounter = 1;
    private int id;
    private boolean isJoint;
    private double balance;
    private List<Customer> attachedUsers = new ArrayList<>();

    public Account() {
        this.isJoint = false;
        this.balance = 0.0;
        this.id = idCounter;
        idCounter++;
    }

    public Account(Customer user) {
        this.isJoint = false;
        this.balance = 0.0;
        this.attachedUsers.add(user);
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

    public Account(boolean isJoint, double balance, Customer user) {
        FundChecker.checkFunds(balance);
        this.isJoint = isJoint;
        this.balance = balance;
        this.attachedUsers.add(user);
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

    public List<Customer> getAttachedUsers() {
        return attachedUsers;
    }
    
    public User getAttachedUser(int index) {
        return attachedUsers.get(index);
    }

    public void setAttachedUsers(List<Customer> attachedUsers) {
        this.attachedUsers = attachedUsers;
    }
    
    public void addAttachedUser(Customer user) {
        this.attachedUsers.add(user);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------\n");
        sb.append(String.format("| %-5s | %-10s | %-10s |\n", "ID", "Joint", "Balance"));
        sb.append(String.format("| %-5d | %-10s | %-10d |\n", id, isJoint, balance));
        return sb.toString();
    }

    public String toString(int index) {
        StringBuffer sb = new StringBuffer();
        sb.append("---------------------------------------\n");
        sb.append(String.format("| %-5s | %-10s | %-10s |\n", "Index", "Joint", "Balance"));
        sb.append(String.format("| %-5d | %-10s | %-10.2f |\n", index, isJoint, balance));
        return sb.toString();
    }

    public String attachedUsersToString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Attached Users:\n");
        for(Customer user : attachedUsers) {
            sb.append(user.getUsername() + "\n");
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attachedUsers == null) ? 0 : attachedUsers.hashCode());
        long temp;
        temp = Double.doubleToLongBits(balance);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + id;
        result = prime * result + (isJoint ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Account other = (Account) obj;
        if (attachedUsers == null) {
            if (other.attachedUsers != null)
                return false;
        } else if (!attachedUsers.equals(other.attachedUsers))
            return false;
        if (Double.doubleToLongBits(balance) != Double.doubleToLongBits(other.balance))
            return false;
        if (id != other.id)
            return false;
        if (isJoint != other.isJoint)
            return false;
        return true;
    }

}
