package com.revature;

import org.junit.Assert;
import org.junit.Test;

import com.revature.models.Admin;
import com.revature.models.Customer;
import com.revature.models.Employee;

public class AccountApplicationTest extends TestBankFuncsSetup {
    
    @Test
    public void customerApplyForAccount() {
        Customer c1 = (Customer) bank.getBankUser("test1");
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank);
        Assert.assertEquals(1, bank.getPendingAccs().size());

        a1.denyAcc(bank, 0);
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
    }

    @Test
    public void customerApplyForAccountUserAttachment() {
        Customer c1 = (Customer) bank.getBankUser("test1");
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank);

        Assert.assertEquals(1, bank.getPendingAccs().get(0).getAttachedUsernames().size());
        Assert.assertEquals(1, bank.getPendingAccs().size());

        a1.denyAcc(bank, 0);
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
    }

    // @Test
    // public void accountApproval() {
    //     Customer c1 = (Customer) bank.getBankUser("john2");
    //     c1.applyAccount(bank);
    //     Account applied = bank.popPending(0);
    //     bank.addApprovedAccs(applied);
    //     c1.addOpenAccount(applied);
    //     c1 = (Customer) bank.getBankUser("john2");

    //     Assert.assertEquals(0, bank.getPendingAccs().size());
    //     Assert.assertEquals(1, bank.getApprovedAccs().size());
    //     Assert.assertEquals(1, c1.getOpenAccounts().size());
    // }

    @Test
    public void accountApprovalEmployee() {
        Customer cust = (Customer) bank.getBankUser("test1");
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        cust.applyAccount(bank);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, cust.getOpenAccounts().size());
        a1.cancelAccount(bank, 1);
    }

    @Test
    public void accountApprovalAdmin() {
        Customer cust = (Customer) bank.getBankUser("test1");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        cust.applyAccount(bank);
        adm.approveAcc(bank, 0);
        
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, cust.getOpenAccounts().size());
        adm.cancelAccount(bank, 1);
    }

    @Test
    public void accountDenialEmployee() {
        Customer cust = (Customer) bank.getBankUser("test1");
        Employee empl = (Employee) bank.getBankUser("test2");
        cust.applyAccount(bank);
        empl.denyAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, cust.getOpenAccounts().size());
    }

    @Test
    public void accountDenialAdmin() {
        Customer cust = (Customer) bank.getBankUser("test1");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        cust.applyAccount(bank);
        adm.denyAcc(bank, 0);
        
        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, cust.getOpenAccounts().size());
    }

}
