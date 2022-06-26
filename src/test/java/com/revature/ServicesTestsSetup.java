package com.revature;

import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;

import com.revature.dao.AccountOperations;
import com.revature.dao.UserOperations;
import com.revature.dao.interfaces.AccountDAO;
import com.revature.dao.interfaces.UserDAO;
import com.revature.services.AccountServices;
import com.revature.services.UserServices;

public class ServicesTestsSetup {

    static UserDAO mockUserDAO;
    static AccountDAO mockAccountDAO;

    static UserServices us;
    static AccountServices as;

    @Before
    public void before() {
        mockUserDAO = mock(UserOperations.class);
        mockAccountDAO = mock(AccountOperations.class);
        us = new UserServices(mockUserDAO);
        as = new AccountServices(mockAccountDAO);
    }

    @After
    public void reset() {
        mockUserDAO = null;
        mockAccountDAO = null;
        us = null;
        as = null;
    }
}
