package com.revature.utils;

import com.revature.exceptions.InvalidUserTypeException;
import com.revature.models.Admin;
import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.models.User;
import com.revature.services.AccountServices;

public class UserFactory {

    public static User getNewUser(String type, String username, String password, String firstname, String lastname, String email, String phone, AccountServices as) {
        switch(type.toLowerCase()) {
            case "customer":
                return new Customer(username, password, firstname, lastname, email, phone, as);
            case "employee":
                return new Employee(username, password, firstname, lastname, email, phone, as);
            case "admin":
                return new Admin(username, password, firstname, lastname, email, phone, as);
            default:
                throw new InvalidUserTypeException();
        }
    }
    
}
