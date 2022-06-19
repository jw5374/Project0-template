package com.revature.models;

public class Employee extends User {

    public Employee(String username, String password) {
        super(username, password);
    }

    public Employee(String username, String password, String firstname, String lastname, String email, String phone) {
        super(username, password, firstname, lastname, email, phone);
    }

    public void approveAcc(Bank bank, int accIndex) {
        Account approved = bank.popPending(accIndex);
        bank.addApprovedAccs(approved);
    }
    
    public void denyAcc(Bank bank, int accIndex) {
        bank.popPending(accIndex);
    }
    
}
