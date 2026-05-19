<%@ page import="com.example.tp9fanfaron.model.Event" %>
<%@ page import="com.example.tp9fanfaron.model.Inscription" %>
<%@ page import="com.example.tp9fanfaron.model.Section" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 08/05/2026
  Time: 16:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Event event = (Event) request.getAttribute("event");
    List<Inscription> inscriptions = (List<Inscription>) request.getAttribute("inscriptions");
    Fanfaron currentUser = (Fanfaron) session.getAttribute("fanfaron");
    Inscription registration = (Inscription) request.getAttribute("registration");
    List<Section> sectionsForFanfaron = (List<Section>) request.getAttribute("sectionsForFanfaron");
%>
<head>
    <title>Inscription pour l'évènement "<%= event == null ? "" : event.getName() %>"</title>
    <style>
        .confirmé {
            color: green;
            font-weight: bold;
        }

        .enattente {
            color: orange;
            font-weight: bold;
        }

        .annulé {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<%
    if (event == null) {
%>
<p>Aucun évènement trouvé.</p>
<% } else {
%>
<h1><%= event.getName() %>
</h1>
<p><strong>Type:</strong> <%= event.getType() %>
</p>
<p><strong>Description:</strong> <%= event.getDescription() %>
</p>
<p><strong>Date:</strong> <%= event.getDateTime() %>
</p>
<p><strong>Durée:</strong> <%= event.getLengthInMinutes() %> minutes</p>
<p><strong>Lieu:</strong> <%= event.getPlace() %>
</p>
<h2>Inscriptions</h2>
<table border="1">
    <tr>
        <th>Nom</th>
        <th>Instrument</th>
        <th>Statut</th>
    </tr>
        <% for (Inscription inscription : inscriptions) { %>
    <tr>
        <td><%= inscription.getName() %>
        </td>
        <td><%= inscription.getInstrument() %>
        </td>
        <!-- usage of status as CSS class -->
        <td class="<%= inscription.getStatus().toLowerCase().replace(" ", "") %>"><%= inscription.getStatus() %>
        </td>
    </tr>
        <% } %>
</table>
<%
    }
%>

        <%
    if (event != null && currentUser != null && registration != null && sectionsForFanfaron != null && !sectionsForFanfaron.isEmpty()) {
%>
    <h2>Modifier ma participation</h2>
    <form action="./controller?action=updateRegistration" method="post">
        <input type="hidden" name="id" value="<%= event.getId() %>">

        <fieldset>
            <legend>Pupitre:</legend>
            <% for (Section section : sectionsForFanfaron) {
                boolean isChecked = section.getName().equals(registration.getInstrument());
            %>
            <div>
                <input type="radio" id="section_<%= section.getId() %>" name="instrument"
                       value="<%= section.getName() %>"
                    <%= isChecked ? "checked" : "" %>>
                <label for="section_<%= section.getId() %>"><%= section.getName() %>
                </label>
            </div>
            <% } %>
        </fieldset>

        <label for="status">Statut:</label>
        <select name="status" id="status">
            <option value="Confirmé" <%= "Confirmé".equals(registration.getStatus()) ? "selected" : "" %>>Confirmé
            </option>
            <option value="En attente" <%= "En attente".equals(registration.getStatus()) ? "selected" : "" %>>En
                attente
            </option>
            <option value="Annulé" <%= "Annulé".equals(registration.getStatus()) ? "selected" : "" %>>Annulé</option>
        </select>
        <br>
        <button type="submit">Mettre à jour</button>
    </form>
        <% }
%>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
