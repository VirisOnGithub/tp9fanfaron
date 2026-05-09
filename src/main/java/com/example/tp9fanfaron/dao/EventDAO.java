package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Event;
import com.example.tp9fanfaron.model.Inscription;
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

    public List<Inscription> findInscriptionsByEventId(int eventId) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "select f.prenom || ' ' || f.nom as personne, i.statut, p.nom from inscrire i, fanfaron f, pupitre p where i.id_technique = f.id_technique and i.id_pupitre = p.id and i.id_evenement = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            List<Inscription> inscriptions = new java.util.ArrayList<>();

            while (rs.next()) {
                Inscription inscription = new Inscription(
                    rs.getString("personne"),
                    rs.getString("nom"),
                    rs.getString("statut")
                );
                inscriptions.add(inscription);
            }

            rs.close();
            ps.close();

            return inscriptions;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void updateInscription(Inscription i, Integer eventId) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "update inscrire set statut = ?, id_pupitre = (select id from pupitre where nom = ?) where id_evenement = ? and id_technique = (select id_technique from fanfaron where prenom || ' ' || nom = ?)";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, i.getStatus());
            ps.setString(2, i.getInstrument());
            ps.setInt(3, eventId);
            ps.setString(4, i.getName());

            int rowsAffected = ps.executeUpdate();
            ps.close();

            if (rowsAffected == 0) {
                throw new SQLException("Aucune inscription trouvée pour l'évènement id : " + eventId + " et le fanfaron : " + i.getName());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Inscription getInscriptionOnEventId(Integer idFanfaron, Integer idEvent) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "select f.prenom || ' ' || f.nom as personne, i.statut, p.nom from inscrire i, fanfaron f, pupitre p where i.id_technique = f.id_technique and i.id_pupitre = p.id and i.id_evenement = ? and f.id_technique = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idEvent);
            ps.setInt(2, idFanfaron);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Inscription inscription = new Inscription(
                    rs.getString("personne"),
                    rs.getString("nom"),
                    rs.getString("statut")
                );
                rs.close();
                ps.close();
                return inscription;
            } else {
                rs.close();
                ps.close();
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Event findById(int eventId) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT * FROM evenement WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Event(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getString("nom"),
                    rs.getObject("date", LocalDateTime.class),
                    rs.getInt("duree"),
                    rs.getString("lieu"),
                    rs.getString("description")
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
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

    public void update(Event event) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = """
                UPDATE evenement
                SET type = ?, nom = ?, date = ?, duree = ?, lieu = ?, description = ?
                WHERE id = ?
                """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, event.getType());
            ps.setString(2, event.getName());
            ps.setObject(3, event.getDateTime());
            ps.setInt(4, event.getLengthInMinutes());
            ps.setString(5, event.getPlace());
            ps.setString(6, event.getDescription());
            ps.setInt(7, event.getId());

            int rowsAffected = ps.executeUpdate();
            ps.close();

            if (rowsAffected == 0) {
                throw new SQLException("Aucun évènement trouvé avec l'id : " + event.getId());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void delete(int eventId) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "DELETE FROM evenement WHERE id = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, eventId);

            int rowsAffected = ps.executeUpdate();
            ps.close();

            if (rowsAffected == 0) {
                throw new SQLException("Aucun évènement trouvé avec l'id : " + eventId);
            }
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
