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
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        a1.denyAcc(bank, 0);
    }
    
    @Test
    public void accountInitializeWithNoBalance() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(0.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        a1.denyAcc(bank, 0);
    }

    @Test
    public void accountInitializedJoint() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsernames().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());

        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(1).getAttachedUsernames().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(2, c2.getOpenAccounts().size());
        a1.cancelAccount(bank, 1);
    }

    @Test
    public void accountJointBalanceChanges() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin empl = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsernames().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test3", applied.getAttachedUsernames().get(1));

        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(0).getAttachedUsernames().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(2, c2.getOpenAccounts().size());

        c1.getOpenAccount(1).withdraw(10);

        Assert.assertEquals(90, c2.getOpenAccount(1).getBalance(), 0.0001);

        c2.getOpenAccount(1).deposit(35);

        Assert.assertEquals(125, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(125, bank.getApprovedAcc(1).getBalance(), 0.0001);
        
        bank.getApprovedAcc(1).withdraw(50);
        
        Assert.assertEquals(75, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(75, c2.getOpenAccount(1).getBalance(), 0.0001);

        empl.cancelAccount(bank, 1);
    
    }

    @Test
    public void accountCancelJoint() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin empl = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, true, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        applied.addAttachedUser(c2);

        Assert.assertEquals(2, applied.getAttachedUsernames().size());
        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));

        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, bank.getApprovedAcc(0).getAttachedUsernames().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(2, c2.getOpenAccounts().size());
        
        empl.cancelAccount(bank, 1);
        
        Assert.assertEquals(1, c1.getOpenAccounts().size());
        Assert.assertEquals(1, c2.getOpenAccounts().size());
        Assert.assertEquals(1, bank.getApprovedAccs().size());
    }

    @Test
    public void depositToAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank);
        Account applied = bank.getPendingAccs().get(0);
        applied.deposit(100);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        a1.denyAcc(bank, 0);
    }

    @Test
    public void withdrawFromAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        
        applied.withdraw(67);

        Assert.assertEquals(33, applied.getBalance(), 0.0001);

        a1.denyAcc(bank, 0);
    }

    @Test
    public void customerTransferFunds() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin a1 = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        
        c1.transferFunds(1, 2, 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(36, c1.getOpenAccount(2).getBalance(), 0.0001);

        a1.cancelAccount(bank, 1);
        a1.cancelAccount(bank, 1);
    }

    @Test
    public void adminTransferFundsSameCustomer() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        adm.approveAcc(bank, 0);
        adm.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        
        adm.transferFunds(bank.getApprovedAcc(1), bank.getApprovedAcc(2), 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(36, c1.getOpenAccount(2).getBalance(), 0.0001);

        adm.cancelAccount(bank, 1);
        adm.cancelAccount(bank, 1);
    }

    @Test
    public void adminTransferFundsDifferentCustomer() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Customer c2 = (Customer) bank.getBankCustomer(1);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c2.applyAccount(bank);
        adm.approveAcc(bank, 0);
        adm.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(2, c2.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c2.getOpenAccount(1).getBalance(), 0.0001);
        
        adm.transferFunds(bank.getApprovedAcc(1), bank.getApprovedAcc(2), 36);
        
        Assert.assertEquals(64, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(36, c2.getOpenAccount(1).getBalance(), 0.0001);

        adm.cancelAccount(bank, 1);
        adm.cancelAccount(bank, 1);
    }

    @Test
    public void adminCloseOpenAccount() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        adm.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(2, bank.getApprovedAccs().size());
        Assert.assertEquals(2, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);

        adm.cancelAccount(bank, 1);

        Assert.assertEquals(1, bank.getApprovedAccs().size());
        Assert.assertEquals(1, c1.getOpenAccounts().size());
    }

    @Test(expected = InsufficientFundsException.class)
    public void customerTransferFundsInsufficient() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        try {
            c1.transferFunds(2, 1, 36);
        } catch (InsufficientFundsException e) {
            adm.cancelAccount(bank, 1);
            adm.cancelAccount(bank, 1);
            throw new InsufficientFundsException();
        }

    }

    @Test(expected = InsufficientFundsException.class)
    public void adminTransferFundsInsufficient() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        try {
            adm.transferFunds(c1.getOpenAccount(2), c1.getOpenAccount(1), 36);
        } catch (InsufficientFundsException e) {
            adm.cancelAccount(bank, 1);
            adm.cancelAccount(bank, 1);
            throw new InsufficientFundsException();
        }

    }

    @Test(expected = InsufficientFundsException.class)
    public void overdrawFromAccountException() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);

        Assert.assertEquals(100.0, applied.getBalance(), 0.0001);
        Assert.assertEquals("test1", applied.getAttachedUsernames().get(0));
        
        applied.withdraw(67);
        
        Assert.assertEquals(33, applied.getBalance(), 0.0001);
        
        applied.withdraw(33);
        
        Assert.assertEquals(0, applied.getBalance(), 0.0001);
        try {
            applied.withdraw(10);
        } catch (InsufficientFundsException e) {
            adm.denyAcc(bank, 0);
            throw new InsufficientFundsException();
        }

    }

    @Test(expected = InvalidFundsException.class)
    public void customerTransferFundsInvalid() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        try {
            c1.transferFunds(0, 1, -36);
        } catch (InvalidFundsException e) {
            adm.cancelAccount(bank, 1);
            adm.cancelAccount(bank, 1);
            throw new InvalidFundsException(-36);
        }
    }

    @Test(expected = InvalidFundsException.class)
    public void adminTransferFundsInvalid() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Employee empl = (Employee) bank.getBankUser("test2");
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        c1.applyAccount(bank);
        empl.approveAcc(bank, 0);
        empl.approveAcc(bank, 0);

        Assert.assertEquals(0, bank.getPendingAccs().size());;
        Assert.assertEquals(3, bank.getApprovedAccs().size());
        Assert.assertEquals(3, c1.getOpenAccounts().size());
        Assert.assertEquals(100.0, c1.getOpenAccount(1).getBalance(), 0.0001);
        Assert.assertEquals(0, c1.getOpenAccount(2).getBalance(), 0.0001);
        try {
            adm.transferFunds(c1.getOpenAccount(2), c1.getOpenAccount(1), -36);
        } catch (InvalidFundsException e) {
            adm.cancelAccount(bank, 1);
            adm.cancelAccount(bank, 1);
            throw new InvalidFundsException(-36);
        }
    }

    @Test(expected = InvalidFundsException.class)
    public void withdrawInvalidInput() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        try {
            applied.withdraw(-1);
        } catch (InvalidFundsException e) {
            adm.denyAcc(bank, 0);
            throw new InvalidFundsException(-1);
        }
    }

    @Test(expected = InvalidFundsException.class)
    public void depositInvalidInput() {
        Customer c1 = (Customer) bank.getBankCustomer(0);
        Admin adm = (Admin) bank.getBankUser("testadmin");
        c1.applyAccount(bank, false, 100.0);
        Account applied = bank.getPendingAccs().get(0);
        try {
            applied.deposit(-1);
        } catch (InvalidFundsException e) {
            adm.denyAcc(bank, 0);
            throw new InvalidFundsException(-1);
        }
    }

}
