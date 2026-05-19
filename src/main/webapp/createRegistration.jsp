<%@ page import="com.example.tp9fanfaron.model.Section" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inscription à un évènement</title>
</head>
<body>
<%
    Integer eventId = (Integer) request.getAttribute("eventId");
    List<Section> sectionsForFanfaron = (List<Section>) request.getAttribute("sectionsForFanfaron");
%>
<h1>Inscription à l'évènement</h1>
<%@ include file="includes/status.jsp" %>

<% if (eventId == null) { %>
<p>Évènement introuvable.</p>
<% } else if (sectionsForFanfaron == null || sectionsForFanfaron.isEmpty()) { %>
<p>Aucun pupitre disponible pour l'inscription.</p>
<% } else { %>
<form action="./controller?action=submitRegistration" method="post">
    <input type="hidden" name="id" value="<%= eventId %>">

    <fieldset>
        <legend>Pupitre:</legend>
        <% for (Section section : sectionsForFanfaron) { %>
        <div>
            <input type="radio" id="section_<%= section.getId() %>" name="pupitreId" value="<%= section.getId() %>" required>
            <label for="section_<%= section.getId() %>"><%= section.getName() %></label>
        </div>
        <% } %>
    </fieldset>

    <label for="status">Statut:</label>
    <select name="status" id="status" required>
        <option value="Confirmé">Confirmé</option>
        <option value="En attente">En attente</option>
        <option value="Annulé">Annulé</option>
    </select>
    <br>
    <button type="submit">S'inscrire</button>
</form>
<% } %>
</body>
</html>
