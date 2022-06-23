package com.revature.utils;

// import java.io.FileReader;
// import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseAccess {
    
    private static Connection dbConn = null;

    private DatabaseAccess() {}

    public static Connection getConnection() {
        try {
            if(dbConn != null && !dbConn.isClosed()) {
                return dbConn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // using a properties file
        // Properties props = new Properties();

        try {
            // props.load(new FileReader("C:\\Users\\aweso\\CodeProjects\\Revature\\assignments\\Project0-template\\src\\main\\resources\\app.properties"));
            // dbConn = DriverManager.getConnection(props.getProperty("url"), props.getProperty("user"), props.getProperty("password"));
            // using system env variables
            dbConn = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
            dbConn.setSchema("Banking");
            return dbConn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
