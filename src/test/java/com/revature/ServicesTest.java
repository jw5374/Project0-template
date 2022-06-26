package com.revature;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.revature.models.Account;
import com.revature.models.Customer;
import com.revature.models.User;

public class ServicesTest extends ServicesTestsSetup {
    
    @Test
    public void insertNewAccountTest() {
        Account acc = new Account();

        int mockedId = (int) Math.random() * 100;

        when(mockAccountDAO.insertAccount(acc)).thenReturn(mockedId);

        Assert.assertEquals(mockedId, as.insertNewAccount(acc));
    }
    
    @Test
    public void fetchingAllAccounts() {
        List<Account> accList = new ArrayList<>();

        when(mockAccountDAO.fetchAllAccounts()).thenReturn(accList);

        Assert.assertEquals(accList, as.retrieveAllAccounts());
    }

    @Test
    public void insertNewUserTest() {
        Customer cust = new Customer("test1", "password", "John", "Smith", "email@email.com", "1234567890", as);

        String mockedUsername = "test1";

        when(mockUserDAO.insertUser(cust, "Customer")).thenReturn(mockedUsername);

        Assert.assertEquals(mockedUsername, us.insertNewUser(cust));
    }

    @Test
    public void fetchingAllUsers() {
        List<User> userList = new ArrayList<>();


        when(mockUserDAO.fetchAllUsers()).thenReturn(userList);

        Assert.assertEquals(userList, us.retrieveAllUsers());
    }
}
