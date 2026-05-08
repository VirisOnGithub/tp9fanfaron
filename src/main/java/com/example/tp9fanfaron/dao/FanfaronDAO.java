package com.example.tp9fanfaron.dao;

import com.example.tp9fanfaron.model.Fanfaron;
import com.example.tp9fanfaron.model.Group;
import com.example.tp9fanfaron.model.Section;
import com.example.tp9fanfaron.utils.DbConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FanfaronDAO {
    private final DbConnectionManager dbConnectionManager;

    private FanfaronDAO(DbConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static final class InstanceHolder {
        static final FanfaronDAO instance = new FanfaronDAO(DbConnectionManager.getInstance());
    }

    public static FanfaronDAO getInstance() {
        return InstanceHolder.instance;
    }

    public Fanfaron findById(Integer id) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT f.*, h.cle FROM fanfaron f, hachage h WHERE id_technique = ? AND f.id_mdp = h.id_mdp";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            Fanfaron fanfaron;

            if (rs.next()) {
                fanfaron = new Fanfaron(
                        rs.getInt("id_technique"),
                        rs.getString("identifiant"),
                        rs.getString("email"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("genre"),
                        rs.getString("contrainte_alimentaire"),
                        rs.getObject("date_creation", LocalDateTime.class),
                        rs.getObject("date_derniere_connexion", LocalDateTime.class),
                        rs.getBoolean("est_admin"),
                        rs.getString("cle")
                );
            } else {
                System.out.println("Fanfaron not found with technical id: " + id);
                return null;
            }

            rs.close();
            ps.close();

            return fanfaron;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void create(Fanfaron fanfaron) throws SQLException {
        try (Connection conn = dbConnectionManager.getConnection()) {

            String queryMdp = "INSERT INTO hachage (cle) VALUES (crypt(?, gen_salt('bf'))) RETURNING id_mdp";

            PreparedStatement psMdp = conn.prepareStatement(queryMdp);

            psMdp.setString(1, fanfaron.getPassword());

            ResultSet rs = psMdp.executeQuery();
            rs.next();
            int idMdp = rs.getInt("id_mdp");

            psMdp.close();

            String query = "INSERT INTO fanfaron (identifiant, email, prenom, nom, genre, contrainte_alimentaire, date_creation, date_derniere_connexion, est_admin, id_mdp) VALUES (?, ?, ?, ?, ?, ?, ?, null, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, fanfaron.getUsername());
            ps.setString(2, fanfaron.getEmail());
            ps.setString(3, fanfaron.getName());
            ps.setString(4, fanfaron.getSurname());
            ps.setString(5, fanfaron.getGender());
            ps.setString(6, fanfaron.getAlimentaryConstraint());
            ps.setObject(7, LocalDateTime.now());
            ps.setBoolean(8, fanfaron.getIsAdmin());
            ps.setInt(9, idMdp);

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<Fanfaron> findAll() {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT f.*, h.cle FROM fanfaron f, hachage h WHERE f.id_mdp = h.id_mdp";

            PreparedStatement ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            List<Fanfaron> fanfarons = new ArrayList<>();

            while (rs.next()) {
                Fanfaron fanfaron = new Fanfaron(
                        rs.getInt("id_technique"),
                        rs.getString("identifiant"),
                        rs.getString("email"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("genre"),
                        rs.getString("contrainte_alimentaire"),
                        rs.getObject("date_creation", LocalDateTime.class),
                        rs.getObject("date_derniere_connexion", LocalDateTime.class),
                        rs.getBoolean("est_admin"),
                        rs.getString("cle")
                );

                fanfarons.add(fanfaron);
            }

            rs.close();
            ps.close();

            return fanfarons;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public void deleteById(Integer id) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "DELETE FROM fanfaron WHERE id_technique = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, id);

            ps.executeUpdate();

            ps.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(Fanfaron fanfaron) throws SQLException {
        System.out.println("update fanfaron with id: " + fanfaron.getId());

        try (Connection conn = dbConnectionManager.getConnection()) {
            PreparedStatement ps;

            if (fanfaron.getPassword() != null) {
                System.out.println("update with new password");
                String queryMdp = "INSERT INTO hachage (cle) VALUES (crypt(?, gen_salt('bf'))) RETURNING id_mdp";

                PreparedStatement psMdp = conn.prepareStatement(queryMdp);

                psMdp.setString(1, fanfaron.getPassword());

                psMdp.executeQuery();

                ResultSet rs = psMdp.getResultSet();
                rs.next();
                int idMdp = rs.getInt("id_mdp");

                psMdp.close();

                String query = "UPDATE fanfaron SET identifiant = ?, email = ?, prenom = ?, nom = ?, genre = ?, contrainte_alimentaire = ?, date_creation = ?, date_derniere_connexion = ?, est_admin = ?, id_mdp = ? WHERE id_technique = ?";

                ps = conn.prepareStatement(query);

                ps.setString(1, fanfaron.getUsername());
                ps.setString(2, fanfaron.getEmail());
                ps.setString(3, fanfaron.getName());
                ps.setString(4, fanfaron.getSurname());
                ps.setString(5, fanfaron.getGender());
                ps.setString(6, fanfaron.getAlimentaryConstraint());
                ps.setObject(7, fanfaron.getCreationDate());
                ps.setObject(8, fanfaron.getLastConnectionDate());
                ps.setBoolean(9, fanfaron.getIsAdmin());
                ps.setInt(10, idMdp);
                ps.setInt(11, fanfaron.getId());
            } else {
                // S'il n'y a pas de changement de mdp
                System.out.println("update without new password");
                String query = "UPDATE fanfaron SET identifiant = ?, email = ?, prenom = ?, nom = ?, genre = ?, contrainte_alimentaire = ?, date_creation = ?, date_derniere_connexion = ?, est_admin = ? WHERE id_technique = ?";

                ps = conn.prepareStatement(query);

                ps.setString(1, fanfaron.getUsername());
                ps.setString(2, fanfaron.getEmail());
                ps.setString(3, fanfaron.getName());
                ps.setString(4, fanfaron.getSurname());
                ps.setString(5, fanfaron.getGender());
                ps.setString(6, fanfaron.getAlimentaryConstraint());
                ps.setObject(7, fanfaron.getCreationDate());
                ps.setObject(8, fanfaron.getLastConnectionDate());
                ps.setBoolean(9, fanfaron.getIsAdmin());
                ps.setInt(10, fanfaron.getId());
            }

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    /*
        * Handle connection for a fanfaron with given username and password.
        * The username can be either the identifiant or the email of the fanfaron.
     */
    public Fanfaron handleConnection(String username, String password) {
        try(Connection conn = dbConnectionManager.getConnection()) {
            String queryLastConnection = "UPDATE fanfaron SET date_derniere_connexion = ? WHERE (identifiant = ? OR email = ?)";

            PreparedStatement psLastConnection = conn.prepareStatement(queryLastConnection);

            psLastConnection.setObject(1, LocalDateTime.now());
            psLastConnection.setString(2, username);
            psLastConnection.setString(3, username);

            psLastConnection.executeUpdate();

            psLastConnection.close();

            String query = "SELECT * FROM fanfaron f JOIN hachage h ON f.id_mdp = h.id_mdp WHERE (f.identifiant = ? OR f.email = ?) AND crypt(?, h.cle) = h.cle";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, username);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Fanfaron(
                        rs.getInt("id_technique"),
                        rs.getString("identifiant"),
                        rs.getString("email"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("genre"),
                        rs.getString("contrainte_alimentaire"),
                        rs.getObject("date_creation", LocalDateTime.class),
                        rs.getObject("date_derniere_connexion", LocalDateTime.class),
                        rs.getBoolean("est_admin"),
                        rs.getString("cle")
                );
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void handleGroupAndSectionInscription(Fanfaron f, List<Group> groups, List<Section> sections) {
        try (Connection conn = dbConnectionManager.getConnection()) {

            // Erase every group and section belonging to the fanfaron
            String queryDeleteSections = "DELETE FROM appartenir WHERE id_technique = ?";
            PreparedStatement psDeleteSections = conn.prepareStatement(queryDeleteSections);
            psDeleteSections.setInt(1, f.getId());
            psDeleteSections.executeUpdate();
            psDeleteSections.close();

            String queryDeleteGroups = "DELETE FROM participer WHERE id_technique = ?";
            PreparedStatement psDeleteGroups = conn.prepareStatement(queryDeleteGroups);
            psDeleteGroups.setInt(1, f.getId());
            psDeleteGroups.executeUpdate();
            psDeleteGroups.close();

            // Sections Registration
            if (sections != null && !sections.isEmpty()) {
                StringBuilder querySections = new StringBuilder("INSERT INTO appartenir VALUES ");
                for (Section _s : sections) {
                    querySections.append("(?, ?),");
                }
                querySections = new StringBuilder(querySections.substring(0, querySections.length() - 1) + ";");

                PreparedStatement psSections = conn.prepareStatement(querySections.toString());

                int i = 1;
                for (Section s : sections) {
                    psSections.setInt(i, f.getId());
                    i++;
                    psSections.setInt(i, s.getId());
                    i++;
                }

                psSections.executeUpdate();

                psSections.close();
            }

            // Groups Registration
            if (groups != null &&!groups.isEmpty()) {
                StringBuilder queryGroups = new StringBuilder("INSERT INTO participer VALUES ");
                for (Group _p : groups) {
                    queryGroups.append("(?, ?),");
                }
                queryGroups = new StringBuilder(queryGroups.substring(0, queryGroups.length() - 1));

                PreparedStatement psGroups = conn.prepareStatement(queryGroups.toString());

                int j = 1;
                for (Group p : groups) {
                    psGroups.setInt(j, f.getId());
                    j++;
                    psGroups.setInt(j, p.getId());
                    j++;
                }

                psGroups.executeUpdate();

                psGroups.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Integer> belongingSectionsIds(Integer idFanfaron) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT id_pupitre FROM appartenir WHERE id_technique = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, idFanfaron);

            ResultSet rs = ps.executeQuery();

            List<Integer> sectionIds = new ArrayList<>();

            while (rs.next()) {
                sectionIds.add(rs.getInt("id_pupitre"));
            }

            rs.close();
            ps.close();

            return sectionIds;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    public List<Integer> belongingGroupsIds(Integer idFanfaron) {
        try (Connection conn = dbConnectionManager.getConnection()) {
            String query = "SELECT id_groupe FROM participer WHERE id_technique = ?";

            PreparedStatement ps = conn.prepareStatement(query);

            ps.setInt(1, idFanfaron);

            ResultSet rs = ps.executeQuery();

            List<Integer> sectionIds = new ArrayList<>();

            while (rs.next()) {
                sectionIds.add(rs.getInt("id_groupe"));
            }

            rs.close();
            ps.close();

            return sectionIds;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return List.of();
        }
    }
}
