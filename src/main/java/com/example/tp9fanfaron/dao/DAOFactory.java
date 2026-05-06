package com.example.tp9fanfaron.dao;

public class DAOFactory {

    public static EventDAO getEventDAO() {
        return EventDAO.getInstance();
    }

    public static SectionDAO getSectionDAO() {
        return SectionDAO.getInstance();
    }

    public static GroupDAO getGroupDAO() {
        return GroupDAO.getInstance();
    }

    public static FanfaronDAO getFanfaronDAO() {
        return FanfaronDAO.getInstance();
    }

    public static HashDAO getHashDAO() {
        return HashDAO.getInstance();
    }
}
