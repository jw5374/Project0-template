package com.revature.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.services.AccountServices;

public class Employee extends User {
    private static Logger logger = LogManager.getLogger(Employee.class);
    
    public Employee(String username, String password) {
        super(username, password);
    }

    public Employee(String username, String password, String firstname, String lastname, String email, String phone,
            AccountServices as) {
        super(username, password, firstname, lastname, email, phone, as);
    }

    public void approveAcc(Bank bank, int accIndex) {
        Account approved = bank.popPending(accIndex);
        approved.setPending(false);
        getAs().updateAccount(approved);
        logger.info(getUsername() + " has approved the account with id: " + approved.getId());
        for(String user : approved.getAttachedUsernames()) {
            Customer cust = (Customer) bank.getBankUser(user);
            cust.addOpenAccount(approved);
            logger.info(cust.getUsername() + " now has the account with id: " + approved.getId());
        }
        bank.addApprovedAccs(approved);
    }
    
    public void denyAcc(Bank bank, int accIndex) {
        int id = bank.popPending(accIndex).getId();
        getAs().deleteAccount(id);
        logger.info(getUsername() + " has denied the account with id: " + id);
    }
    
}
