package com.revature.models;

public abstract class User {
    private String username;
    private String password;
    private Account[] openAccounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public Account[] getOpenAccounts() {
        return openAccounts;
    }

    public void setOpenAccounts(Account... openAccounts) {
        this.openAccounts = openAccounts;
    }
    
    
}
