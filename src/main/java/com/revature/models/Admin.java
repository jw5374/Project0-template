package com.revature.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.services.AccountServices;

public class Admin extends Employee {
    private static Logger logger = LogManager.getLogger(Admin.class);

    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin(String username, String password, String firstname, String lastname, String email, String phone,
            AccountServices as) {
        super(username, password, firstname, lastname, email, phone, as);
    }

    public void transferFunds(Account ac1, Account ac2, double amt) {
        ac1.withdraw(amt);
        ac2.deposit(amt);
        logger.info("Admin: " + getUsername() + " has transferred $" + amt + " from Account " + ac1.getId() + " to Account " + ac2.getId());
    }

    public void cancelAccount(Bank bank, int index) {
        Account closed = bank.popApproved(index);
        getAs().deleteAccount(closed.getId());
        logger.info("Admin: " + getUsername() + " has closed Account " + closed.getId());
        for(String user : closed.getAttachedUsernames()) {
            Customer cust = (Customer) bank.getBankUser(user);
            cust.removeOpenAccount(closed.getId());
            logger.info(cust.getUsername() + " has lost Account " + closed.getId());
        }
        
    }

}
