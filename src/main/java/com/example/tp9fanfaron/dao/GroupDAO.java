package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.utils.DbConnectionManager;

public class GroupDAO {
    private final DbConnectionManager dbConnectionManager;

    private GroupDAO(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static final class InstanceHolder {
        static final GroupDAO instance = new GroupDAO(DbConnectionManager.getInstance());
    }

    public static GroupDAO getInstance() {
        return GroupDAO.InstanceHolder.instance;
    }
}
