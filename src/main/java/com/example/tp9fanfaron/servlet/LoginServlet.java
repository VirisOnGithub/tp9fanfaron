package com.example.tp9fanfaron.servlet;

import com.example.tp9fanfaron.dao.DAOFactory;
import com.example.tp9fanfaron.model.Fanfaron;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet: doPost called");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Fanfaron f = DAOFactory.getFanfaronDAO().handleConnection(username, password);

        if (f != null) {
            req.getSession().setAttribute("fanfaron", f);
            resp.sendRedirect("./controller?action=" + (f.getIsAdmin() ? "admin" : "me"));
        } else {
            // we send a GET request to login, with error
            String errorMsg = URLEncoder.encode("Identifiant ou mot de passe incorrect.", StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/controller?action=login&error=" + errorMsg);
        }
    }
}
