package com.revature;

import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.revature.models.*;

public class TestBankFuncs {
    
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

    /************************** Initialization testing *****************************/ 

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

    /************************** Initialization testing *****************************/ 



    @Test
    public void customerApplyForAccount() {
        Customer c1 = (Customer) bank.getBankUser("john2");
        c1.applyAccount(bank);
        Assert.assertEquals(1, bank.getPendingAccs().size());
    }

    @Test
    public void customerApplyForAccountUserAttachment() {
        Customer c1 = (Customer) bank.getBankUser("john2");
        c1.applyAccount(bank);
        Assert.assertEquals("User [email=jt@email.com, firstname=John, lastname=Thomas, phone=9876543210, username=john2]", bank.popPending(0).getAttachedUsers().get(0).toString());
        Assert.assertEquals(0, bank.getPendingAccs().size());
    }

    @Test
    public void accountApproval() {
        Customer c1 = (Customer) bank.getBankUser("john2");
        c1.applyAccount(bank);
        Account applied = bank.popPending(0);
        bank.addApprovedAccs(applied);
        c1.addOpenAccount(applied);
        c1 = (Customer) bank.getBankUser("john2");
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals("User [email=jt@email.com, firstname=John, lastname=Thomas, phone=9876543210, username=john2]", c1.getOpenAccount(0).getAttachedUser(0).toString());
    }

    @Test
    public void accountApprovalEmployee() {
        Customer cust = (Customer) bank.getBankUser("john1");
        Employee empl = (Employee) bank.getBankUser("jane1");
        cust.applyAccount(bank);
        empl.approveAcc(bank, 0, "john1");
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, cust.getOpenAccounts().size());
        Assert.assertEquals("User [email=js@email.com, firstname=John, lastname=Smith, phone=2345678910, username=john1]", cust.getOpenAccount(0).getAttachedUser(0).toString());
    }

    @Test
    public void accountApprovalAdmin() {
        Customer cust = (Customer) bank.getBankUser("john1");
        Admin adm = (Admin) bank.getBankUser("james1");
        cust.applyAccount(bank);
        adm.approveAcc(bank, 0, "john1");
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, cust.getOpenAccounts().size());
        Assert.assertEquals("User [email=js@email.com, firstname=John, lastname=Smith, phone=2345678910, username=john1]", cust.getOpenAccount(0).getAttachedUser(0).toString());
    }

    @Test
    public void accountDenialEmployee() {
        Customer cust = (Customer) bank.getBankUser("john1");
        Employee empl = (Employee) bank.getBankUser("jane1");
        cust.applyAccount(bank);
        empl.denyAcc(bank, 0);
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(0, bank.getApprovedAccs().size());
        Assert.assertEquals(0, cust.getOpenAccounts().size());
    }

    @Test
    public void accountDenialAdmin() {
        Customer cust = (Customer) bank.getBankUser("john1");
        Admin adm = (Admin) bank.getBankUser("james1");
        cust.applyAccount(bank);
        adm.denyAcc(bank, 0);
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(0, bank.getApprovedAccs().size());
        Assert.assertEquals(0, cust.getOpenAccounts().size());
    }

}
