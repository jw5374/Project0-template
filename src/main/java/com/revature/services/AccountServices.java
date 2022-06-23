package com.revature.services;

import java.util.List;

import com.revature.dao.AccountOperations;
import com.revature.dao.interfaces.AccountDAO;
import com.revature.models.Account;

public class AccountServices {
    private static AccountDAO ao = new AccountOperations();

    public static int insertNewAccount(Account acc) {
        return ao.insertAccount(acc);
    }

    public static List<Account> retrieveAllAccounts() {
        return ao.fetchAllAccounts();
    }

    public static void updateAccount(Account acc) {
        ao.updateAccount(acc);
    }

    public static void deleteAccount(int accId) {
        ao.deleteAccount(accId);
    }
}
