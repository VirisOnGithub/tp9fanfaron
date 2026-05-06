package com.example.tp9fanfaron.servlet;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.Fanfaron;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Fanfaron f = DAOFactory.getFanfaronDAO().handleConnection(username, password);

        if (f != null) {
            req.getSession().setAttribute("fanfaron", f);
            resp.sendRedirect("./?action=" + (f.getIsAdmin() ? "admin" : "me"));
        } else {
            req.setAttribute("error", "Identifiant ou mot de passe incorrect.");
            req.getRequestDispatcher("./?action=login").forward(req, resp);
        }
    }
}
