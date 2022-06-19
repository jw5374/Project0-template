package com.revature;

import org.junit.Assert;
import org.junit.Test;

import com.revature.exceptions.InsufficientFundsException;
import com.revature.exceptions.InvalidFundsException;
import com.revature.models.Account;
import com.revature.models.Admin;
import com.revature.models.Customer;
import com.revature.models.Employee;

public class AccountModificationTest extends TestBankFuncsSetup {
    
    @Test
    public void accountInitializeWithBalance() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
    }
    
    @Test
    public void accountInitializeWithNoBalance() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(0.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
    }

    @Test
    public void accountInitializedJoint() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Employee empl = (Employee) bank.getBankUser("jane1");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsers().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
        Assert.assertEquals(0, c1.getOpenAccounts().size());
        Assert.assertEquals(0, c2.getOpenAccounts().size());

        empl.approveAcc(bank, 0, "john1", "john2");

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(0).getAttachedUsers().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());
    }

    @Test
    public void accountJointBalanceChanges() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin empl = (Admin) bank.getBankUser("james1");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsers().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());

        empl.approveAcc(bank, 0, "john1", "john2");

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(0).getAttachedUsers().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());

        c1.getOpenAccount(0).withdraw(10);

        Assert.assertEquals(90, c2.getOpenAccount(0).getBalance(), 0.0001);

        c2.getOpenAccount(0).deposit(35);

        Assert.assertEquals(125, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(125, bank.getApprovedAcc(0).getBalance(), 0.0001);
        
        bank.getApprovedAcc(0).withdraw(50);
        
        Assert.assertEquals(75, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(75, c2.getOpenAccount(0).getBalance(), 0.0001);
    
    }

    @Test
    public void accountCancelJoint() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin empl = (Admin) bank.getBankUser("james1");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsers().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());

        empl.approveAcc(bank, 0, "john1", "john2");

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(0).getAttachedUsers().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());
        
        empl.cancelAccount(bank, 0);
        
        Assert.assertEquals(0, c1.getOpenAccounts().size());
        Assert.assertEquals(0, c2.getOpenAccounts().size());
        Assert.assertEquals(0, bank.getApprovedAccs().size());
    }

    @Test
    public void depositToAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank);
        Account applied = bank.getPendingAccs().get(0);
        applied.deposit(100);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
    }

    @Test
    public void withdrawFromAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
        
        applied.withdraw(67);

        Assert.assertEquals(33, applied.getBalance(), 0.0001);
    }

    @Test
    public void customerTransferFunds() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("jane1");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0, "john1");
        empl.approveAcc(bank, 0, "john1");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(1).getBalance(), 0.0001);
        
        c1.transferFunds(0, 1, 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(36, c1.getOpenAccount(1).getBalance(), 0.0001);
    }

    @Test
    public void adminTransferFundsSameCustomer() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("james1");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        adm.approveAcc(bank, 0, "john1");
        adm.approveAcc(bank, 0, "john1");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(1).getBalance(), 0.0001);
        
        adm.transferFunds(bank.getApprovedAcc(0), bank.getApprovedAcc(1), 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(36, c1.getOpenAccount(1).getBalance(), 0.0001);
    }

    @Test
    public void adminTransferFundsDifferentCustomer() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin adm = (Admin) bank.getBankUser("james1");
        c1.applyAccount(bank, false, 100.0);
        c2.applyAccount(bank);
        adm.approveAcc(bank, 0, "john1");
        adm.approveAcc(bank, 0, "john2");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(0, c2.getOpenAccount(0).getBalance(), 0.0001);
        
        adm.transferFunds(bank.getApprovedAcc(0), bank.getApprovedAcc(1), 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(36, c2.getOpenAccount(0).getBalance(), 0.0001);
    }

    @Test
    public void adminCloseOpenAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("james1");
        c1.applyAccount(bank, false, 100.0);
        adm.approveAcc(bank, 0, "john1");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);

        adm.cancelAccount(bank, 0);

        Assert.assertEquals(0, bank.getApprovedAccs().size());
        Assert.assertEquals(0, c1.getOpenAccounts().size());
    }

    @Test(expected = InsufficientFundsException.class)
    public void customerTransferFundsInsufficient() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("jane1");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0, "john1");
        empl.approveAcc(bank, 0, "john1");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(1).getBalance(), 0.0001);
        
        c1.transferFunds(1, 0, 36);
    }

    @Test(expected = InsufficientFundsException.class)
    public void overdrawFromAccountException() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("john1", applied.getAttachedUser(0).getUsername());
        
        applied.withdraw(67);
        
        Assert.assertEquals(33, applied.getBalance(), 0.0001);
        
        applied.withdraw(33);
        
        Assert.assertEquals(0, applied.getBalance(), 0.0001);
        
        applied.withdraw(10);
    }

    @Test(expected = InvalidFundsException.class)
    public void customerTransferFundsInvalid() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("jane1");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0, "john1");
        empl.approveAcc(bank, 0, "john1");

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(0).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(1).getBalance(), 0.0001);
        
        c1.transferFunds(0, 1, -36);
    }

    @Test(expected = InvalidFundsException.class)
    public void withdrawInvalidInput() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.withdraw(-1);
    }

    @Test(expected = InvalidFundsException.class)
    public void depositInvalidInput() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.deposit(-1);
    }

}
