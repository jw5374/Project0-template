package com.revature.utils;

import java.util.List;

import com.revature.dao.AccountOperations;
import com.revature.dao.UserOperations;
import com.revature.dao.interfaces.AccountDAO;
import com.revature.dao.interfaces.UserDAO;
import com.revature.models.Account;
import com.revature.models.Bank;
import com.revature.models.Customer;
import com.revature.models.User;

public class BankSetup {
    private static AccountDAO ao = new AccountOperations();
    private static UserDAO uo = new UserOperations();

    public static Bank setupBank() {
        Bank bank = new Bank();
        List<Account> allAcc = ao.fetchAllAccounts();
        List<User> allUsers = uo.fetchAllUsers(); 
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
