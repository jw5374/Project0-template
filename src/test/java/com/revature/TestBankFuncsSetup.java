package com.revature;

import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;

import com.revature.services.AccountServices;
import com.revature.services.UserServices;
import com.revature.utils.BankSetup;
import com.revature.dao.AccountOperations;
import com.revature.dao.UserOperations;
import com.revature.models.Bank;

public class TestBankFuncsSetup {
    
    static BankSetup bset;
    static Bank bank;
    static UserServices us;
    static AccountServices as;
    static Connection conn;

    @Before
    public void newbank() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            bset = new BankSetup(conn);
            conn.setSchema("Banking");
            us = new UserServices(new UserOperations(conn));
            as = new AccountServices(new AccountOperations(conn));
            bank = bset.setupBank();
        } catch (SQLException e) {/* ignored */}
    }

    @After
    public void clear() {
        try {
            bank = null;
            bset = null;
            us = null;
            as = null;
            conn.close();
            conn = null;
        } catch (SQLException e) {}
    }

}
