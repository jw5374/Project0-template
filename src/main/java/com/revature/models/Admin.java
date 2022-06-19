package com.revature.models;

public class Admin extends Employee {

    public Admin(String username, String password) {
        super(username, password);
    }
    
    public Admin(String username, String password, String firstname, String lastname, String email, String phone) {
        super(username, password, firstname, lastname, email, phone);
    }

    public void transferFunds(Account ac1, Account ac2, double amt) {
        ac1.withdraw(amt);
        ac2.deposit(amt);
    }

}
