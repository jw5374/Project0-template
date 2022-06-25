package com.revature.utils;

import java.sql.Connection;
import java.util.List;

import com.revature.dao.AccountOperations;
import com.revature.dao.UserOperations;
import com.revature.models.Account;
import com.revature.models.Bank;
import com.revature.models.Customer;
import com.revature.models.User;
import com.revature.services.AccountServices;
import com.revature.services.UserServices;

public class BankSetup {

    private AccountServices as;
    private UserServices us;

    public BankSetup(Connection conn) {
        this.as = new AccountServices(new AccountOperations(conn));
        this.us = new UserServices(new UserOperations(conn));
    }

    public Bank setupBank() {
        Bank bank = new Bank();
        List<Account> allAcc = as.retrieveAllAccounts();
        List<User> allUsers = us.retrieveAllUsers(); 
        for(User u : allUsers) {
            bank.addBankUser(u.getUsername(), u);
        }

        for(Account a : allAcc) {
            if(a.isPending()) {
                bank.addPendingAccs(a);
                continue;
            }
            for(String user : a.getAttachedUsernames()) {
                Customer cust = (Customer) bank.getBankUser(user);
                cust.addOpenAccount(a);
            }
            bank.addApprovedAccs(a);
        }
        return bank;
    }
}
