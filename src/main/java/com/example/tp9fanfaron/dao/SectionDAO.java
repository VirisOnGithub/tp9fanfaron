package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.utils.DbConnectionManager;

public class SectionDAO {
    private final DbConnectionManager dbConnectionManager;

    private SectionDAO(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static final class InstanceHolder {
        static final SectionDAO instance = new SectionDAO(DbConnectionManager.getInstance());
    }

    public static SectionDAO getInstance() {
        return SectionDAO.InstanceHolder.instance;
    }
}
