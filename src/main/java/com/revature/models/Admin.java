package com.revature.models;

public class Admin extends Employee {

    public Admin(String username, String password) {
        super(username, password);
    }
    
    public void transferFunds(Account ac1, Account ac2, double amt) {
        ac1.withdraw(amt);
        ac2.deposit(amt);
    }

}
