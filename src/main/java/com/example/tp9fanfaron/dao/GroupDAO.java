package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Fanfaron;
import com.example.tp9fanfaron.model.Group;
import com.example.tp9fanfaron.utils.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<Group> findAll() {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT * FROM groupe g";

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            List<Group> groups = new ArrayList<>();

            while (rs.next()) {
                Group group = new Group(
                        rs.getInt("id_technique"),
                        rs.getString("nom")
                );

                groups.add(group);
            }

            rs.close();
            ps.close();

            return groups;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
