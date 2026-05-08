package com.example.tp9fanfaron.controler;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.Event;
import com.example.tp9fanfaron.model.Fanfaron;
import com.example.tp9fanfaron.model.Group;
import com.example.tp9fanfaron.model.Section;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@WebServlet("/")
public class Controleur extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Fanfaron fSession = (Fanfaron) req.getSession().getAttribute("fanfaron");
//        System.out.println(fSession);
        boolean sessionIsAdmin = fSession != null && fSession.getIsAdmin();
        String vue = "";
        if (action == null) {
            vue = sessionIsAdmin ? "admin.jsp" : "login.jsp";
        } else {
            switch (action) {
                case "login":
                    vue = "login.jsp";
                    break;
                case "signup":
                    vue = "signup.jsp";
                    break;
                case "admin":
                    if (sessionIsAdmin) {
                        req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                        vue = "admin.jsp";
                    } else {
                        req.setAttribute("error", "Vous devez être administrateur pour accéder à cette page.");
                        vue = "login.jsp";
                    }
                    break;
                case "delete":
                    if (sessionIsAdmin) {
                        String idStr = req.getParameter("id");
                        if (idStr != null) {
                            try {
                                int id = Integer.parseInt(idStr);
                                DAOFactory.getFanfaronDAO().deleteById(id);
                            } catch (NumberFormatException e) {
                                req.setAttribute("error", "ID invalide : " + idStr);
                            } finally {
                                req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                                vue = "admin.jsp";
                            }
                        } else {
                            req.setAttribute("error", "ID manquant pour la suppression.");
                            vue = "admin.jsp";
                        }
                    } else {
                        req.setAttribute("error", "Vous devez être administrateur pour supprimer un fanfaron.");
                        vue = "login.jsp";
                    }
                    break;

                case "edit":
                    if (sessionIsAdmin) {
                        String idStr = req.getParameter("id");
                        if (idStr != null) {
                            try {
                                int id = Integer.parseInt(idStr);
                                Fanfaron f = DAOFactory.getFanfaronDAO().findById(id);
                                if (f != null) {
                                    req.setAttribute("fanfaron", f);
                                    vue = "edit.jsp";
                                } else {
                                    req.setAttribute("error", "Fanfaron non trouvé avec l'ID : " + id);
                                    req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                                    vue = "admin.jsp";
                                }
                            } catch (NumberFormatException e) {
                                req.setAttribute("error", "ID invalide : " + idStr);
                                req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                                vue = "admin.jsp";
                            }
                        } else {
                            req.setAttribute("error", "ID manquant pour l'édition.");
                            req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                            vue = "admin.jsp";
                        }
                    } else {
                        req.setAttribute("error", "Vous devez être administrateur pour éditer un fanfaron.");
                        vue = "login.jsp";
                    }
                    break;

                case "logout":
                    req.getSession().invalidate();
                    vue = "login.jsp";
                    break;

                case "me":
                    if (fSession != null) {
                        req.setAttribute("fanfaron", fSession);
                        req.setAttribute("isMemberOfPrestationGroup", DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId()));
                        vue = "me.jsp";
                    } else {
                        req.setAttribute("error", "Vous devez être connecté pour accéder à cette page.");
                        vue = "login.jsp";
                    }
                    break;

                case "editGroups":
                    if (fSession != null) {
                        req.setAttribute("fanfaron", fSession);
                        req.setAttribute("groups", DAOFactory.getGroupDAO().findAll());
                        req.setAttribute("sections", DAOFactory.getSectionDAO().findAll());
                        req.setAttribute("fanfaronGroups", DAOFactory.getFanfaronDAO().belongingGroupsIds(fSession.getId()));
                        req.setAttribute("fanfaronSections", DAOFactory.getFanfaronDAO().belongingSectionsIds(fSession.getId()));
                        vue = "editGroups.jsp";
                    } else {
                        req.setAttribute("error", "Vous devez être connecté pour accéder à cette page.");
                        vue = "login.jsp";
                    }
                    break;

                case "createEvent":
                    if (DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId())) {
                        vue = "createEvent.jsp";
                    } else {
                        req.setAttribute("error", "Vous devez appartenir au groupe de la commission prestation pour accéder à cette page.");
                        vue = "400.jsp";
                    }
                    break;

                case "events":
                    if (fSession != null) {
                        List<Event> events = null;
                        try {
                            events = DAOFactory.getEventDAO().findAll();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        req.setAttribute("events", events);
                        vue = "events.jsp";
                    } else {
                        req.setAttribute("error", "Vous devez être connecté pour accéder à cette page.");
                        vue = "login.jsp";
                    }
                    break;

                default:
                    req.setAttribute("error", "Action inconnue : " + action);
                    vue = sessionIsAdmin ? "admin.jsp" : "login.jsp";
            }
        }
        req.getRequestDispatcher(vue).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Fanfaron fSession = (Fanfaron) req.getSession().getAttribute("fanfaron");
        if (action == null) {
            req.setAttribute("error", "Action manquante dans la requête.");
            req.getRequestDispatcher("400.jsp").forward(req, resp);
            return;
        }
        String vue = "";
        switch (action) {
            case "edit":
                Integer id = Integer.parseInt(req.getParameter("id"));
                String username = req.getParameter("username");
                String name = req.getParameter("name");
                String surname = req.getParameter("surname");
                String email = req.getParameter("email");
                String password = req.getParameter("password");
                String gender = req.getParameter("gender");
                String alimentaryConstraint = req.getParameter("alimentaryConstraint");
                boolean isAdmin = "on".equals(req.getParameter("isAdmin")); // une checkbox renvoie "on" par défaut si cochée

                Fanfaron f = new Fanfaron(id, username, email, name, surname, gender, alimentaryConstraint, null, null, isAdmin, password.isEmpty() ? null : password);
                try {
                    DAOFactory.getFanfaronDAO().update(f);
                } catch (SQLException e) {
                    req.setAttribute("error", "Erreur lors de la mise à jour du fanfaron : " + e.getMessage());
                } finally {
                    req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
                    vue = "admin.jsp";
                }
                break;
            case "updateGroups":
                Integer fId = Integer.parseInt(req.getParameter("id"));
                Fanfaron dummyFanfaron = new Fanfaron(fId, null, null, null, null, null, null, null, null, false, null);
                String[] groupIdsStr = req.getParameterValues("groupIds");
                String[] sectionIdsStr = req.getParameterValues("sectionIds");
                List<Group> groups = null;
                List<Section> sections = null;

                if (groupIdsStr != null) {
                    groups = Arrays.stream(groupIdsStr).map((String idStr) -> {
                        try {
                            int groupId = Integer.parseInt(idStr);
                            return new Group(groupId, null);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                }

                if (sectionIdsStr != null) {
                    sections = Arrays.stream(sectionIdsStr).map((String idStr) -> {
                        try {
                            int sectionId = Integer.parseInt(idStr);
                            return new Section(sectionId, null);
                        } catch (NumberFormatException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).collect(Collectors.toList());
                }

                try {
                    DAOFactory.getFanfaronDAO().handleGroupAndSectionInscription(dummyFanfaron, groups, sections);
                } finally {
                    Fanfaron updatedF = DAOFactory.getFanfaronDAO().findById(fId);
                    req.getSession().setAttribute("fanfaron", updatedF); // Met à jour la session avec les nouvelles infos
                    req.setAttribute("fanfaron", updatedF);
                    req.setAttribute("groups", DAOFactory.getGroupDAO().findAll());
                    req.setAttribute("sections", DAOFactory.getSectionDAO().findAll());
                    req.setAttribute("fanfaronGroups", DAOFactory.getFanfaronDAO().belongingGroupsIds(fId));
                    req.setAttribute("fanfaronSections", DAOFactory.getFanfaronDAO().belongingSectionsIds(fId));
                    vue = "editGroups.jsp";
                }
                break;

            case "createEvent":
                String eventType = req.getParameter("type");
                String eventName = req.getParameter("name");
                LocalDateTime eventDate = LocalDateTime.parse(req.getParameter("date"));
                Integer eventDuration = Integer.parseInt(req.getParameter("duration"));
                String eventLocation = req.getParameter("location");
                String eventDescription = req.getParameter("description");

                Event event = new Event(null, eventType, eventName, eventDate, eventDuration, eventLocation, eventDescription);
                try {
                    DAOFactory.getEventDAO().create(event);
                    req.setAttribute("success", "Événement créé avec succès !");
                } catch (SQLException e) {
                    req.setAttribute("error", "Erreur lors de la création de l'événement : " + e.getMessage());
                } finally {
                    req.setAttribute("fanfaron", fSession);
                    req.setAttribute("isMemberOfPrestationGroup", DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId()));
                    vue = "me.jsp";
                }
                break;

            default:
                req.setAttribute("error", "Action inconnue : " + action);
                vue = "400.jsp";
                break;
        }
        req.getRequestDispatcher(vue).forward(req, resp);
    }
}
