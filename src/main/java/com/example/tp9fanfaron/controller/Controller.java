package com.example.tp9fanfaron.controller;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.*;
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

@WebServlet("/controller")
public class Controller extends HttpServlet {

    ///// UTILS

    /**
     * Gets the logged-in fanfaron from session
     */
    private Fanfaron getSessionFanfaron(HttpServletRequest req) {
        return (Fanfaron) req.getSession().getAttribute("fanfaron");
    }

    /**
     * checks session
     */
    private boolean requireLogin(HttpServletRequest req, Fanfaron fSession) {
        if (fSession == null) {
            req.setAttribute("error", "Vous devez être connecté pour accéder à cette page.");
            return false;
        }
        return true;
    }

    /**
     * checks admin status of the session
     */
    private boolean requireAdmin(HttpServletRequest req, Fanfaron fSession) {
        if (fSession == null || !fSession.getIsAdmin()) {
            req.setAttribute("error", "Vous devez être administrateur pour accéder à cette page.");
            return false;
        }
        return true;
    }

    /**
     * checks if the session belongs to the prestation group
     */
    private boolean requirePrestation(HttpServletRequest req, Fanfaron fSession) {
        if (!requireLogin(req, fSession)) return false;
        if (!DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId())) {
            req.setAttribute("error", "Vous devez appartenir au groupe de la commission prestation pour accéder à cette page.");
            return false;
        }
        return true;
    }

    ///// DATA LOADING

    /**
     * loads all events from the database
     */
    private List<Event> loadAllEvents() throws ServletException {
        try {
            return DAOFactory.getEventDAO().findAll();
        } catch (SQLException e) {
            throw new ServletException("Erreur lors du chargement des événements.", e);
        }
    }

    /**
     * take the user to the events page
     */
    private String forwardToEvents(HttpServletRequest req) throws ServletException {
        req.setAttribute("events", loadAllEvents());
        return "events.jsp";
    }

    /**
     * take the user to the admin page
     */
    private String forwardToAdmin(HttpServletRequest req) {
        req.setAttribute("fanfaronList", DAOFactory.getFanfaronDAO().findAll());
        req.setAttribute("sections", DAOFactory.getSectionDAO().findAll());
        req.setAttribute("groups", DAOFactory.getGroupDAO().findAll());
        return "admin.jsp";
    }

    /**
     * parse Integer from request, returning it
     */
    private int parseIntParam(HttpServletRequest req, String paramName) {
        String raw = req.getParameter(paramName);
        if (raw == null) {
            throw new IllegalArgumentException("Paramètre manquant : " + paramName);
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Paramètre invalide (" + paramName + ") : " + raw);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        Fanfaron fSession = getSessionFanfaron(req);
        boolean sessionIsAdmin = fSession != null && fSession.getIsAdmin();

        String vue;

        // if at the root :
        // admin => admin page
        // other => login page
        if (action == null) {
            vue = sessionIsAdmin ? "admin.jsp" : "login.jsp";
        } else {
            vue = switch (action) {
                case "login"   -> "login.jsp";
                case "signup"  -> "signup.jsp";
                case "logout"  -> handleLogout(req);
                case "admin"   -> handleAdminGet(req, fSession);
                case "delete"  -> handleDeleteFanfaron(req, fSession);
                case "edit"    -> handleEditFanfaronGet(req, fSession);
                case "me"      -> handleMeGet(req, fSession);
                case "editGroups"      -> handleEditGroupsGet(req, fSession);
                case "createEvent"     -> handleCreateEventGet(req, fSession);
                case "events"          -> handleEventsGet(req, fSession);
                case "eventDetails"    -> handleEventDetailsGet(req, fSession);
                case "deleteEvent"     -> handleDeleteEventGet(req, fSession);
                case "editEvent"       -> handleEditEventGet(req, fSession);
                case "editRegistration"-> handleEditRegistrationGet(req, fSession);
                case "registerEvent"   -> handleRegisterEventGet(req, fSession);
                default -> {
                    req.setAttribute("error", "Action inconnue : " + action);
                    yield sessionIsAdmin ? "admin.jsp" : "login.jsp";
                }
            };
        }

        req.getRequestDispatcher(vue).forward(req, resp);
    }

    /**
     * Invalidate user session
     */
    private String handleLogout(HttpServletRequest req) {
        req.getSession().invalidate();
        return "login.jsp";
    }

    /**
     * take the user to the admin page if admin, else to login
     */
    private String handleAdminGet(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        return forwardToAdmin(req);
    }

    /**
     * delete the queried fanfaron if admin, else login
     */
    private String handleDeleteFanfaron(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        try {
            int id = parseIntParam(req, "id");
            DAOFactory.getFanfaronDAO().deleteById(id);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        return forwardToAdmin(req);
    }

    /**
     * take the user to edit page, require admin
     */
    private String handleEditFanfaronGet(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        try {
            int id = parseIntParam(req, "id");
            Fanfaron f = DAOFactory.getFanfaronDAO().findById(id);
            if (f == null) {
                req.setAttribute("error", "Fanfaron non trouvé avec l'ID : " + id);
                return forwardToAdmin(req);
            }
            req.setAttribute("fanfaron", f);
            return "edit.jsp";
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return forwardToAdmin(req);
        }
    }

    /**
     * take the user to his profile page, require login
     */
    private String handleMeGet(HttpServletRequest req, Fanfaron fSession) {
        if (!requireLogin(req, fSession)) return "login.jsp";
        req.setAttribute("fanfaron", fSession);
        req.setAttribute("isMemberOfPrestationGroup",
                DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId()));
        return "me.jsp";
    }

    /**
     * allows user to edit his groups and sections, require login
     */
    private String handleEditGroupsGet(HttpServletRequest req, Fanfaron fSession) {
        if (!requireLogin(req, fSession)) return "login.jsp";
        req.setAttribute("fanfaron", fSession);
        req.setAttribute("groups", DAOFactory.getGroupDAO().findAll());
        req.setAttribute("sections", DAOFactory.getSectionDAO().findAll());
        req.setAttribute("fanfaronGroups", DAOFactory.getFanfaronDAO().belongingGroupsIds(fSession.getId()));
        req.setAttribute("fanfaronSections", DAOFactory.getFanfaronDAO().belongingSectionsIds(fSession.getId()));
        return "editGroups.jsp";
    }

    /**
     * take the user to the event creation page, require prestation group membership
     */
    private String handleCreateEventGet(HttpServletRequest req, Fanfaron fSession) {
        if (!requirePrestation(req, fSession)) return "400.jsp";
        return "createEvent.jsp";
    }

    /**
     * take the user to the events page, require login
     */
    private String handleEventsGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        return forwardToEvents(req);
    }

    /**
     * take the user to the event details page, require login
     */
    private String handleEventDetailsGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            Event event = DAOFactory.getEventDAO().findById(eventId);
            if (event == null) {
                req.setAttribute("error", "Événement non trouvé avec l'ID : " + eventId);
                return forwardToEvents(req);
            }
            List<Inscription> inscriptions = DAOFactory.getEventDAO().findInscriptionsByEventId(eventId);
            Inscription registration = DAOFactory.getEventDAO().getInscriptionOnEventId(fSession.getId(), eventId);
            List<Section> sectionsForFanfaron = DAOFactory.getSectionDAO().findByFanfaronId(fSession.getId());
            req.setAttribute("event", event);
            req.setAttribute("inscriptions", inscriptions);
            req.setAttribute("registration", registration);
            req.setAttribute("sectionsForFanfaron", sectionsForFanfaron);
            return "eventDetails.jsp";
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return forwardToEvents(req);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors du chargement des inscriptions.", e);
        }
    }

    /**
     * delete the queried event, require admin
     */
    private String handleDeleteEventGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            DAOFactory.getEventDAO().delete(eventId);
            req.setAttribute("success", "Événement supprimé avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
        return forwardToEvents(req);
    }

    /**
     * take the user to the event edit page, require login
     */
    private String handleEditEventGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            Event event = DAOFactory.getEventDAO().findById(eventId);
            if (event == null) {
                req.setAttribute("error", "Événement non trouvé avec l'ID : " + eventId);
                return forwardToEvents(req);
            }
            req.setAttribute("event", event);
            return "editEvent.jsp";
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return forwardToEvents(req);
        }
    }

    /**
     * take the user to the registration edit page, require login
     */
    private String handleEditRegistrationGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            int fanfaronId = fSession.getId();
            Inscription inscription = DAOFactory.getEventDAO().getInscriptionOnEventId(fanfaronId, eventId);
            List<Section> sectionsForFanfaron = DAOFactory.getSectionDAO().findByFanfaronId(fanfaronId);
            req.setAttribute("registration", inscription);
            req.setAttribute("eventId", eventId);
            req.setAttribute("sectionsForFanfaron", sectionsForFanfaron);
            return "editRegistration.jsp";
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return forwardToEvents(req);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors du chargement de l'inscription.", e);
        }
    }

    /**
     * take the user to the registration creation page, require login and pupitre membership, if already registered take him to the edit page
     */
    private String handleRegisterEventGet(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            int fanfaronId = fSession.getId();
            Inscription existing = DAOFactory.getEventDAO().getInscriptionOnEventId(fanfaronId, eventId);
            if (existing != null) {
                req.setAttribute("info", "Vous êtes déjà inscrit, vous pouvez modifier votre participation.");
                return handleEditRegistrationGet(req, fSession);
            }

            List<Section> sectionsForFanfaron = DAOFactory.getSectionDAO().findByFanfaronId(fanfaronId);
            if (sectionsForFanfaron == null || sectionsForFanfaron.isEmpty()) {
                req.setAttribute("error", "Vous devez appartenir à un pupitre pour vous inscrire.");
                return forwardToEvents(req);
            }

            req.setAttribute("eventId", eventId);
            req.setAttribute("sectionsForFanfaron", sectionsForFanfaron);
            return "createRegistration.jsp";
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            return forwardToEvents(req);
        } catch (SQLException e) {
            throw new ServletException("Erreur lors du chargement de l'inscription.", e);
        }
    }

    /////// POST

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        Fanfaron fSession = getSessionFanfaron(req);

        if (action == null) {
            req.setAttribute("error", "Action manquante dans la requête.");
            req.getRequestDispatcher("400.jsp").forward(req, resp);
            return;
        }

        String vue = switch (action) {
            case "edit"               -> handleEditFanfaronPost(req, fSession);
            case "updateGroups"       -> handleUpdateGroupsPost(req, fSession);
            case "createEvent"        -> handleCreateEventPost(req, fSession);
            case "editEvent"          -> handleEditEventPost(req, fSession);
            case "updateRegistration" -> handleUpdateRegistrationPost(req, fSession);
            case "submitRegistration" -> handleSubmitRegistrationPost(req, fSession);
            case "createSection"      -> handleCreateSectionPost(req, fSession);
            case "deleteSection"      -> handleDeleteSectionPost(req, fSession);
            case "createGroup"        -> handleCreateGroupPost(req, fSession);
            case "deleteGroup"        -> handleDeleteGroupPost(req, fSession);
            default -> {
                req.setAttribute("error", "Action inconnue : " + action);
                yield "400.jsp";
            }
        };

        req.getRequestDispatcher(vue).forward(req, resp);
    }

    private String handleEditFanfaronPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        try {
            int id = parseIntParam(req, "id");
            String username           = req.getParameter("username");
            String name               = req.getParameter("name");
            String surname            = req.getParameter("surname");
            String email              = req.getParameter("email");
            String password           = req.getParameter("password");
            String gender             = req.getParameter("gender");
            String alimentaryConstraint = req.getParameter("alimentaryConstraint");
            boolean isAdmin           = "on".equals(req.getParameter("isAdmin"));

            Fanfaron f = new Fanfaron(
                    id, username, email, name, surname, gender,
                    alimentaryConstraint, null, null,
                    isAdmin, password.isEmpty() ? null : password
            );
            DAOFactory.getFanfaronDAO().update(f);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la mise à jour du fanfaron : " + e.getMessage());
        }
        return forwardToAdmin(req);
    }

    private String handleUpdateGroupsPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireLogin(req, fSession)) return "login.jsp";
        int fId = parseIntParam(req, "id");
        Fanfaron dummyFanfaron = new Fanfaron(fId, null, null, null, null, null, null, null, null, false, null);

        List<Group> groups = parseIdsToEntities(
                req.getParameterValues("groupIds"),
                id -> new Group(id, null)
        );
        List<Section> sections = parseIdsToEntities(
                req.getParameterValues("sectionIds"),
                id -> new Section(id, null)
        );

        try {
            DAOFactory.getFanfaronDAO().handleGroupAndSectionInscription(dummyFanfaron, groups, sections);
        } catch (Exception e) {
            req.setAttribute("error", "Erreur lors de la mise à jour des groupes : " + e.getMessage());
        }

        Fanfaron updatedF = DAOFactory.getFanfaronDAO().findById(fId);
        req.getSession().setAttribute("fanfaron", updatedF);
        req.setAttribute("fanfaron", updatedF);
        req.setAttribute("groups", DAOFactory.getGroupDAO().findAll());
        req.setAttribute("sections", DAOFactory.getSectionDAO().findAll());
        req.setAttribute("fanfaronGroups", DAOFactory.getFanfaronDAO().belongingGroupsIds(fId));
        req.setAttribute("fanfaronSections", DAOFactory.getFanfaronDAO().belongingSectionsIds(fId));
        return "editGroups.jsp";
    }

    private String handleCreateEventPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requirePrestation(req, fSession)) return "400.jsp";
        try {
            Event event = buildEventFromRequest(req, null);
            DAOFactory.getEventDAO().create(event);
            req.setAttribute("success", "Événement créé avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la création de l'événement : " + e.getMessage());
        }
        req.setAttribute("fanfaron", fSession);
        req.setAttribute("isMemberOfPrestationGroup",
                DAOFactory.getGroupDAO().belongsToPrestation(fSession.getId()));
        return "me.jsp";
    }

    private String handleEditEventPost(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            Event updatedEvent = buildEventFromRequest(req, eventId);
            DAOFactory.getEventDAO().update(updatedEvent);
            req.setAttribute("success", "Événement mis à jour avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
        return forwardToEvents(req);
    }

    private String handleUpdateRegistrationPost(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int idEvent = parseIntParam(req, "id");
            String nameFanfaron = fSession.getName() + " " + fSession.getSurname();
            String nameSection  = req.getParameter("instrument");
            String newStatus    = req.getParameter("status");
            Inscription inscription = new Inscription(nameFanfaron, nameSection, newStatus);
            DAOFactory.getEventDAO().updateInscription(inscription, idEvent);
            req.setAttribute("success", "Inscription mise à jour avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la mise à jour de l'inscription : " + e.getMessage());
        }
        return forwardToEvents(req);
    }

    private String handleSubmitRegistrationPost(HttpServletRequest req, Fanfaron fSession) throws ServletException {
        if (!requireLogin(req, fSession)) return "login.jsp";
        try {
            int eventId = parseIntParam(req, "id");
            int pupitreId = parseIntParam(req, "pupitreId");
            String status = req.getParameter("status");
            DAOFactory.getEventDAO().createInscription(fSession.getId(), eventId, pupitreId, status);
            req.setAttribute("success", "Inscription créée avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la création de l'inscription : " + e.getMessage());
        }
        return forwardToEvents(req);
    }

    private String handleCreateSectionPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        String name = req.getParameter("sectionName");
        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("error", "Nom de section manquant.");
            return forwardToAdmin(req);
        }
        try {
            DAOFactory.getSectionDAO().create(name.trim());
            req.setAttribute("success", "Section ajoutée avec succès !");
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la création de la section : " + e.getMessage());
        }
        return forwardToAdmin(req);
    }

    private String handleDeleteSectionPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        try {
            int id = parseIntParam(req, "sectionId");
            DAOFactory.getSectionDAO().deleteById(id);
            req.setAttribute("success", "Section supprimée avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la suppression de la section : " + e.getMessage());
        }
        return forwardToAdmin(req);
    }

    private String handleCreateGroupPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        String name = req.getParameter("groupName");
        if (name == null || name.trim().isEmpty()) {
            req.setAttribute("error", "Nom de groupe manquant.");
            return forwardToAdmin(req);
        }
        try {
            DAOFactory.getGroupDAO().create(name.trim());
            req.setAttribute("success", "Groupe ajouté avec succès !");
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la création du groupe : " + e.getMessage());
        }
        return forwardToAdmin(req);
    }

    private String handleDeleteGroupPost(HttpServletRequest req, Fanfaron fSession) {
        if (!requireAdmin(req, fSession)) return "login.jsp";
        try {
            int id = parseIntParam(req, "groupId");
            DAOFactory.getGroupDAO().deleteById(id);
            req.setAttribute("success", "Groupe supprimé avec succès !");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Erreur lors de la suppression du groupe : " + e.getMessage());
        }
        return forwardToAdmin(req);
    }

    ////// UTILS

    /**
     * Construit un objet {@link Event} à partir des paramètres de la requête.
     *
     * @param id null pour une création, non-null pour une mise à jour
     */
    private Event buildEventFromRequest(HttpServletRequest req, Integer id) {
        String type        = req.getParameter("type");
        String name        = req.getParameter("name");
        LocalDateTime date = LocalDateTime.parse(req.getParameter("date"));
        int duration       = parseIntParam(req, "duration");
        String location    = req.getParameter("location");
        String description = req.getParameter("description");
        return new Event(id, type, name, date, duration, location, description);
    }

    /**
     * Convertit un tableau de chaînes d'identifiants en liste d'entités via une factory.
     * Les valeurs non parsables sont silencieusement ignorées.
     *
     * @param ids     tableau de chaînes (peut être null si aucune case cochée)
     * @param factory fonction qui crée une entité à partir d'un entier
     * @return liste d'entités (vide si ids est null)
     */
    private <T> List<T> parseIdsToEntities(String[] ids, java.util.function.IntFunction<T> factory) {
        if (ids == null) return List.of();
        return Arrays.stream(ids)
                .map(s -> {
                    try { return factory.apply(Integer.parseInt(s)); }
                    catch (NumberFormatException e) { return null; }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}