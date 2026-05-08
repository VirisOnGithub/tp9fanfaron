package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Event;
import com.example.tp9fanfaron.utils.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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

    public void create(Event event) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "INSERT INTO evenement(type, nom, date, duree, lieu, description) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, event.getType());
            ps.setString(2, event.getName());
            ps.setObject(3, event.getDateTime());
            ps.setInt(4, event.getLengthInMinutes());
            ps.setString(5, event.getPlace());
            ps.setString(6, event.getDescription());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<Event> findAll() throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT * FROM evenement";

            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            List<Event> events = new java.util.ArrayList<>();

            while (rs.next()) {
                Event event = new Event(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("nom"),
                    rs.getObject("date", LocalDateTime.class),
                    rs.getInt("duree"),
                    rs.getString("lieu"),
                    rs.getString("description")
                );
                events.add(event);
            }

            rs.close();
            ps.close();

            return events;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
