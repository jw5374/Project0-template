package com.revature;

import static org.mockito.Mockito.mock;

import com.revature.dao.interfaces.AccountDAO;
import com.revature.dao.interfaces.UserDAO;
import com.revature.models.Bank;
import com.revature.services.AccountServices;
import com.revature.services.UserServices;

public class ServicesTestsSetup {

    static UserDAO mockUserDAO = mock(UserDAO.class);
    static AccountDAO mockAccountDAO = mock(AccountDAO.class);

    static Bank bank;
    static UserServices us = new UserServices(mockUserDAO);
    static AccountServices as = new AccountServices(mockAccountDAO);

    
}
