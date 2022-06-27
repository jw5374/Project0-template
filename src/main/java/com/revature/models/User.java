package com.revature.models;

import com.revature.services.AccountServices;

public abstract class User {

    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private AccountServices as;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String firstname, String lastname, String email, String phone, AccountServices as) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.as = as;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public AccountServices getAs() {
        return as;
    }

    public void setAs(AccountServices as) {
        this.as = as;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("--------------------------------------------------------------------------------\n");
        sb.append(String.format("| %-15s | %-15s | %-15s | %-25s | %-15s |\n", "Username", "First Name", "Last Name", "Email", "Phone"));
        sb.append(String.format("| %-15s | %-15s | %-15s | %-25s | %-15s |\n", username, firstname, lastname, email, phone));
       
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
}
