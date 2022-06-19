package com.revature;

import org.junit.Before;
import org.junit.After;

import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.models.Admin;
import com.revature.models.Bank;

public class TestBankFuncsSetup {
    
    static Bank bank;

    @Before
    public void newbank() {
        bank = new Bank();
        bank.addBankUser("john1", new Customer("john1", "bfdhsa", "John", "Smith", "js@email.com", "2345678910"));
        bank.addBankUser("john2", new Customer("john2", "bfdhsa", "John", "Thomas", "jt@email.com", "9876543210"));
        bank.addBankUser("jane1", new Employee("jane1", "bfdhsa"));
        bank.addBankUser("james1", new Admin("james1", "bfdhsa"));
    }

    @After
    public void clear() {
        bank = null;
    }

}
