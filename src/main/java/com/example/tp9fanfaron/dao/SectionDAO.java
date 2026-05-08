package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Section;
import com.example.tp9fanfaron.utils.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
}
