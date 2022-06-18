package com.revature;

import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;


import com.revature.models.*;

public class TestBankFuncs {
    
    static Bank bank;
    static List<Account> pending;
    static List<Account> open;
    static List<Customer> customers;
    static HashMap<String, User> bankUsers;

    @BeforeClass
    public static void setup() {
        bank = new Bank();
        bank.addBankUser("john1", new Customer("john1", "bfdhsa"));
        bank.addBankUser("john2", new Customer("john2", "bfdhsa"));
        bank.addBankUser("jane1", new Employee("jane1", "bfdhsa"));
        bank.addBankUser("james1", new Admin("james1", "bfdhsa"));
        pending = bank.getPendingAccs();
        open = bank.getApprovedAccs();
        customers = bank.getBankCustomers();
        bankUsers = bank.getBankUsers();
    }

    @Before
    public void clear() {
        bank = new Bank(pending, open, customers, bankUsers);
    }

    @Test
    public void initializedUsersTest() {
        Assert.assertEquals(bank.getBankUsers().size(), 4);
    }

    @Test
    public void initializedCustomersTest() {
        Assert.assertEquals(bank.getBankCustomers().size(), 2);
    }

}
