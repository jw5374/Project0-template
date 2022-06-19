package com.revature;

import org.junit.Assert;
import org.junit.Test;

import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.models.Customer;
import com.revature.models.Employee;

public class UserRegistrationTest extends TestBankFuncsSetup {
    
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

    @Test(expected = UserAlreadyExistsException.class)
    public void registerWithExistingUsername() {
        bank.addBankUser("john2", new Customer("John3", "csafe", "John", "Connor", "jc@email.com", "4738629505"));
    }


}
