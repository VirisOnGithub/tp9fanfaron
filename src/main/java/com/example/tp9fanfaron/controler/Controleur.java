package com.example.tp9fanfaron.controler;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.Fanfaron;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

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
            default:
                req.setAttribute("error", "Action inconnue : " + action);
                vue = "400.jsp";
                break;
        }
        req.getRequestDispatcher(vue).forward(req, resp);
    }
}
