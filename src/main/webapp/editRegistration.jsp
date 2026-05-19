<%@ page import="com.example.tp9fanfaron.model.Inscription" %>
<%@ page import="com.example.tp9fanfaron.model.Section" %>
<%@ page import="java.util.List" %>
<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 09/05/2026
  Time: 08:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification de la participation</title>
</head>
<body>
<%
    Inscription inscription = (Inscription) request.getAttribute("registration");
    Integer eventId = (Integer) request.getAttribute("eventId");
    List<Section> sectionsForFanfaron = (List<Section>) request.getAttribute("sectionsForFanfaron");
    if (inscription == null) {
%>
<p>Aucune inscription trouvée.</p>
<% } else { %>
<h1>Modifier la participation de <%= inscription.getName() %></h1>
<%@ include file="includes/status.jsp" %>
<form action="./controller?action=updateRegistration" method="post">
    <input type="hidden" name="id" value="<%= eventId %>">

    <fieldset>
      <legend>Pupitre:</legend>

      <%
            for (Section section : sectionsForFanfaron) {
                boolean isChecked = inscription.getInstrument().equals(section.getName());
      %>
      <div>
        <input type="radio" id="section_<%= section.getId() %>" name="instrument" value="<%= section.getName() %>"
               <%= isChecked ? "checked" : "" %>>
        <label for="section_<%= section.getId() %>"><%= section.getName() %></label>
      </div>
      <% } %>
    </fieldset>

    <label for="status">Statut:</label>
    <select name="status" id="status">
        <option value="Confirmé" <%= "Confirmé".equals(inscription.getStatus()) ? "selected" : "" %>>Confirmé</option>
        <option value="En attente" <%= "En attente".equals(inscription.getStatus()) ? "selected" : "" %>>En attente</option>
        <option value="Annulé" <%= "Annulé".equals(inscription.getStatus()) ? "selected" : "" %>>Annulé</option>
    </select>
    <br>
    <button type="submit">Mettre à jour</button>
</form>
<%
    }
%>
</body>
</html>
