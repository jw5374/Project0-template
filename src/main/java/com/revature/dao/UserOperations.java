package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.dao.interfaces.UserDAO;
import com.revature.models.Admin;
import com.revature.models.Customer;
import com.revature.models.Employee;
import com.revature.models.User;
import com.revature.utils.DatabaseAccess;

public class UserOperations implements UserDAO {

    @Override
    public String insertUser(User user, String type) {
        try (Connection conn = DatabaseAccess.getConnection()) {
            String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING users.username";
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFirstname());
            stmt.setString(4, user.getLastname());
            stmt.setString(5, user.getEmail());
            stmt.setString(6, user.getPhone());
            stmt.setObject(7, type, Types.OTHER);

            ResultSet res = stmt.executeQuery();
            if(res != null) {
                res.next();
                String usern = res.getString("username");
                return usern;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<User> fetchAllUsers() {
        try (Connection conn = DatabaseAccess.getConnection()) {
            List<User> fetched = new ArrayList<>();
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM users";

            ResultSet res = stmt.executeQuery(sql);
            while(res.next()) {
                User user = null;
                String type = res.getString("usertype");
                switch(type) {
                    case "Customer":
                        user = new Customer(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
                        break;
                    case "Employee":
                        user = new Employee(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
                        break;
                    case "Admin":
                        user = new Admin(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
                        break;
                }
                fetched.add(user);
            }
            return fetched;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
