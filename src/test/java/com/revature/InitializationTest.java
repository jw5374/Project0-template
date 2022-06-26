package com.revature;

import org.junit.Test;

import com.revature.exceptions.UserDoesNotExistException;

import org.junit.Assert;

public class InitializationTest extends TestBankFuncsSetup {

    @Test
    public void initializedUsersTest() {
        Assert.assertEquals(4, bank.getBankUsers().size());
    }

    @Test
    public void initializedCustomersTest() {
        Assert.assertEquals(2, bank.getBankCustomers().size());
    }

    @Test
    public void customerInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("--------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-15s | %-15s | %-15s | %-25s | %-15s |\n", "Username", "First Name", "Last Name", "Email", "Phone"));
        sb.append(String.format("| %-15s | %-15s | %-15s | %-25s | %-15s |\n", "test3", "jane", "smith", "email3@email.com", "1234567892"));
        Assert.assertEquals(sb.toString(), bank.getBankCustomer(1).toString());
    }

    @Test
    public void employeeInfo() {
        Assert.assertEquals("test2", bank.getBankUser("test2").getUsername());
        Assert.assertEquals("password", bank.getBankUser("test2").getPassword());
        Assert.assertEquals("john", bank.getBankUser("test2").getFirstname());
        Assert.assertEquals("wayne", bank.getBankUser("test2").getLastname());
        Assert.assertEquals("email2@email.com", bank.getBankUser("test2").getEmail());
        Assert.assertEquals("1234567891", bank.getBankUser("test2").getPhone());
    }

    @Test
    public void adminInfo() {
        Assert.assertEquals("testadmin", bank.getBankUser("testadmin").getUsername());
        Assert.assertEquals("password", bank.getBankUser("testadmin").getPassword());
        Assert.assertEquals("james", bank.getBankUser("testadmin").getFirstname());
        Assert.assertEquals("rutherford", bank.getBankUser("testadmin").getLastname());
        Assert.assertEquals("email3@email.com", bank.getBankUser("testadmin").getEmail());
        Assert.assertEquals("1234567893", bank.getBankUser("testadmin").getPhone());
    }

    @Test
    public void initialPendingAccounts() {
        Assert.assertEquals(0, bank.getPendingAccs().size());
    }
    
    @Test
    public void initialApprovedAccounts() {
        Assert.assertEquals(1, bank.getApprovedAccs().size());
    }

    @Test(expected = UserDoesNotExistException.class)
    public void noUser() {
        bank.getBankUser("bob");
    }
    
}
