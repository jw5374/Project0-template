package com.revature;

import org.junit.Assert;
import org.junit.Test;

import com.revature.models.Customer;
import com.revature.models.Employee;

public class UserRegistrationTests extends TestBankFuncsSetup {
    
    @Test
    public void registerUserCustomer() {
        bank.addBankUser("jane2", new Customer("jane2", "bfdhsa", "Jane", "Smith", "js2@email.com", "2345678911"));

        Assert.assertEquals(3, bank.getBankCustomers().size());
        Assert.assertEquals(5, bank.getBankUsers().size());
    }    

    @Test
    public void registerUser() {
        bank.addBankUser("jane2", new Employee("jane2", "bfdhsa"));

        Assert.assertEquals(2, bank.getBankCustomers().size());
        Assert.assertEquals(5, bank.getBankUsers().size());
    }    


}
