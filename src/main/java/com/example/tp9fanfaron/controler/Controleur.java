package com.example.tp9fanfaron.controler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/")
public class Controleur extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        Boolean sessionIsAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        String vue = "";
        if (action == null) {
            vue = Boolean.TRUE.equals(sessionIsAdmin) ? "admin.jsp" : "login.jsp"; // evite les NullPointerException
        } else {
            //
        }
        req.getRequestDispatcher(vue).forward(req, resp);
    }
}
