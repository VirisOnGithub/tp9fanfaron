package com.example.tp9fanfaron.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/devdb";
    private static final String USER = "webuser";
    private static final String PASSWORD = "webpass";

    private DbConnectionManager() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver problem", e);
        }
    }

    private static final class InstanceHolder {
        static final DbConnectionManager instance = new DbConnectionManager();
    }

    public static DbConnectionManager getInstance() {
        return InstanceHolder.instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}