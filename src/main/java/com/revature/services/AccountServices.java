package com.revature.services;

import java.util.List;

import com.revature.dao.interfaces.AccountDAO;
import com.revature.models.Account;

public class AccountServices {
    private AccountDAO ao;

    public AccountServices(AccountDAO ao) {
        this.ao = ao;
    }

    public int insertNewAccount(Account acc) {
        return ao.insertAccount(acc);
    }

    public List<Account> retrieveAllAccounts() {
        return ao.fetchAllAccounts();
    }

    public void updateAccount(Account acc) {
        ao.updateAccount(acc);
    }

    public void deleteAccount(int accId) {
        ao.deleteAccount(accId);
    }
}
