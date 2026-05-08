package com.example.tp9fanfaron.servlet;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.Fanfaron;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String gender = req.getParameter("gender");
        String dietaryConstraints = req.getParameter("dietaryConstraints");
        Boolean isAdmin = "on".equals(req.getParameter("isAdmin"));

        Fanfaron connectedFanfaron = (Fanfaron) req.getSession().getAttribute("fanfaron");

        // This fanfaron has its plain password as password, but the DAO will hash it before saving it to the database
        Fanfaron f = new Fanfaron(0, username, email, name, surname, gender, dietaryConstraints, null, null, isAdmin && connectedFanfaron != null && connectedFanfaron.getIsAdmin(), password);
        try {
            DAOFactory.getFanfaronDAO().create(f);
            resp.sendRedirect(connectedFanfaron != null && connectedFanfaron.getIsAdmin() ? "./?action=admin" : "./?action=me");
        } catch (SQLException e) {
            e.printStackTrace();
            req.setAttribute("error", "Une erreur est survenue lors de la création du compte. Veuillez réessayer.");
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.setAttribute("name", name);
            req.setAttribute("surname", surname);
            req.setAttribute("dietaryConstraints", dietaryConstraints);
            req.setAttribute("gender", gender);
            req.getRequestDispatcher("signup.jsp").forward(req, resp);
        }
    }
}
