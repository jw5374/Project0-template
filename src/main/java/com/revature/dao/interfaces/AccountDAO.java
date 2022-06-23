package com.revature.dao.interfaces;

import java.util.List;

import com.revature.models.Account;

public interface AccountDAO {

    int insertAccount(Account acc);

    List<Account> fetchAllAccounts();

    void updateAccount(Account acc);

    void deleteAccount(int accId);

}
