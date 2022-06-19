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
        Assert.assertEquals("User [email=jt@email.com, firstname=John, lastname=Thomas, phone=9876543210, username=john2]", bank.getBankCustomer(1).toString());
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
