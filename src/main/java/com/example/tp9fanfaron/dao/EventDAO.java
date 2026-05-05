package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.utils.DbConnectionManager;

public class EventDAO {
    private final DbConnectionManager dbConnectionManager;

    private EventDAO(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static final class InstanceHolder {
        static final EventDAO instance = new EventDAO(DbConnectionManager.getInstance());
    }

    public static EventDAO getInstance() {
        return EventDAO.InstanceHolder.instance;
    }
}
