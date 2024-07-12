package com.revature.project0.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {

    private static final Properties props = new Properties();
    static {
        try (InputStream input = ConnectionUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find database.properties");
            }
            props.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(props.getProperty("DBurl"), props.getProperty("DBusername"), props.getProperty("DBpassword"));
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
        return conn;
    }
}