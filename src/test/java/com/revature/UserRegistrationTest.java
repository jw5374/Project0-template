package com.revature;

import org.junit.Assert;
import org.junit.Test;

import com.revature.exceptions.InvalidUserTypeException;
import com.revature.exceptions.UserAlreadyExistsException;
import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.models.User;
import com.revature.utils.UserFactory;

public class UserRegistrationTest extends TestBankFuncsSetup {
    
    @Test
    public void registerUserCustomer() {
        Customer cust = new Customer("jane2", "bfdhsa", "Jane", "Smith", "js2@email.com", "2345678911", as);
        bank.addBankUser("jane2", cust);
        us.insertNewUser(cust);

        Assert.assertEquals(3, bank.getBankCustomers().size());
        Assert.assertEquals(5, bank.getBankUsers().size());
        Assert.assertEquals("js2@email.com", bank.getBankUser("jane2").getEmail());

        bank.removeBankUser("jane2");
        us.removeUser("jane2");
        Assert.assertEquals(2, bank.getBankCustomers().size());
        Assert.assertEquals(4, bank.getBankUsers().size());
    }    

    @Test
    public void registerUser() {
        Employee empl = new Employee("jane2", "bfdhsa", "Jane", "Smith", "js2@email.com", "2345678911", as);
        bank.addBankUser("jane2", empl);
        us.insertNewUser(empl);

        Assert.assertEquals(2, bank.getBankCustomers().size());
        Assert.assertEquals(5, bank.getBankUsers().size());

        bank.removeBankUser("jane2");
        us.removeUser("jane2");
        Assert.assertEquals(2, bank.getBankCustomers().size());
        Assert.assertEquals(4, bank.getBankUsers().size());
    }

    @Test
    public void userFactoryUse() {
        User factoryget = UserFactory.getNewUser("customer", "test1", "password", "john", "smith", "email@email.com", "1234567890", as);
        User factorygetempl = UserFactory.getNewUser("employee", "test2", "password", "john", "smith", "email@email.com", "1234567890", as);
        User factorygetadm = UserFactory.getNewUser("admin", "test3", "password", "john", "smith", "email@email.com", "1234567890", as);

        Assert.assertEquals("test1", factoryget.getUsername());
        Assert.assertEquals("test2", factorygetempl.getUsername());
        Assert.assertEquals("test3", factorygetadm.getUsername());
        Assert.assertEquals("Customer", factoryget.getClass().getSimpleName());
        Assert.assertEquals("Employee", factorygetempl.getClass().getSimpleName());
        Assert.assertEquals("Admin", factorygetadm.getClass().getSimpleName());
    }

    @Test(expected = InvalidUserTypeException.class)
    public void invalidUserTypeFactory() {
        UserFactory.getNewUser("something", "test1", "password", "john", "smith", "email@email.com", "1234567890", as);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void registerWithExistingUsername() {
        bank.addBankUser("test1", new Customer("John3", "csafe", "John", "Connor", "jc@email.com", "4738629505", as));
    }


}
