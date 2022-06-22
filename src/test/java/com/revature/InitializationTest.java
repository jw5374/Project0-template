package com.revature;

import org.junit.Test;

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
        sb.append(String.format("| %-15s | %-15s | %-15s | %-25s | %-15s |\n", "john2", "John", "Thomas", "jt@email.com", "9876543210"));
        Assert.assertEquals(sb.toString(), bank.getBankCustomer(1).toString());
    }

    @Test
    public void employeeInfo() {
        Assert.assertEquals("jane1", bank.getBankUser("jane1").getUsername());
    }

    @Test
    public void adminInfo() {
        Assert.assertEquals("james1", bank.getBankUser("james1").getUsername());
    }

    @Test
    public void initialPendingAccounts() {
        Assert.assertEquals(0, bank.getPendingAccs().size());
    }
    
    @Test
    public void initialApprovedAccounts() {
        Assert.assertEquals(0, bank.getApprovedAccs().size());
    }
    
}
