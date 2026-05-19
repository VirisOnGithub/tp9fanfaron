package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Section;
import com.example.tp9fanfaron.utils.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Section> findAll() {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT * FROM pupitre p";

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            List<Section> sections = new ArrayList<>();

            while (rs.next()) {
                Section section = new Section(
                        rs.getInt("id"),
                        rs.getString("nom")
                );

                sections.add(section);
            }

            rs.close();
            ps.close();

            return sections;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Section> findByFanfaronId(int fanfaronId) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "select p.id, p.nom from pupitre p, appartenir a where p.id = a.id_pupitre and a.id_technique = ?";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, fanfaronId);

            ResultSet rs = ps.executeQuery();

            List<Section> sections = new ArrayList<>();

            while (rs.next()) {
                Section section = new Section(
                        rs.getInt("id"),
                        rs.getString("nom")
                );

                sections.add(section);
            }

            rs.close();
            ps.close();

            return sections;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public void create(String name) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String checkExisting = "select 1 from pupitre where lower(nom) = lower(?)";
            try (PreparedStatement psCheck = conn.prepareStatement(checkExisting)) {
                psCheck.setString(1, name);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        throw new SQLException("Une section avec ce nom existe déjà.");
                    }
                }
            }

            String insert = "insert into pupitre(id, nom) values ((select coalesce(max(id), 0) + 1 from pupitre), ?)";
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, name);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void deleteById(int id) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "delete from pupitre where id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Aucune section trouvée avec l'id : " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
