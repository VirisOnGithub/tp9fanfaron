package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.utils.DbConnectionManager;

public class HashDAO {
    private final DbConnectionManager dbConnectionManager;

    private HashDAO(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static final class InstanceHolder {
        static final HashDAO instance = new HashDAO(DbConnectionManager.getInstance());
    }

    public static HashDAO getInstance() {
        return HashDAO.InstanceHolder.instance;
    }
}
