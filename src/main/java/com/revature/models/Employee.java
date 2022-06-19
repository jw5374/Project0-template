package com.revature.models;

public class Employee extends User {

    public Employee(String username, String password) {
        super(username, password);
    }

    public Employee(String username, String password, String firstname, String lastname, String email, String phone) {
        super(username, password, firstname, lastname, email, phone);
    }

    public void approveAcc(Bank bank, int accIndex, String... username) {
        Account approved = bank.popPending(accIndex);
        for(String user : username) {
            Customer cust = (Customer) bank.getBankUser(user);
            cust.addOpenAccount(approved);
        }
        bank.addApprovedAccs(approved);
    }
    
    public void denyAcc(Bank bank, int accIndex) {
        bank.popPending(accIndex);
    }
    
}
