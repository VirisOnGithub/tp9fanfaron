<%@ page import="com.example.tp9fanfaron.model.Fanfaron" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.tp9fanfaron.model.Group" %>
<%@ page import="com.example.tp9fanfaron.model.Section" %><%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 06/05/2026
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification des groupes et des pupitres</title>
</head>
<body>
<%
    Fanfaron fanfaron = (Fanfaron) request.getSession().getAttribute("fanfaron");
    List<Group> groups = (List<Group>) request.getAttribute("groups");
    List<Section> sections = (List<Section>) request.getAttribute("sections");
    List<Integer> fanfaronGroupIds = (List<Integer>) request.getAttribute("fanfaronGroups");
    List<Integer> fanfaronSectionIds = (List<Integer>) request.getAttribute("fanfaronSections");
%>
<h1>Inscription aux différents groupes et pupitres</h1>
<%@ include file="includes/status.jsp" %>
<form action="./controller?action=updateGroups" method="post">
    <input type="hidden" name="id" value="<%= fanfaron.getId() %>" />
    <h2>Groupes</h2>
    <%
        for (Group group : groups) {
          boolean isChecked = fanfaronGroupIds.contains(group.getId());
    %>
    <div>
        <input type="checkbox" id="groupId<%=group.getId()%>" name="groupIds" value="<%= group.getId() %>" <%= isChecked ? "checked" : "" %> />
        <label for="groupId<%=group.getId()%>"><%= group.getName() %></label>
    </div>
    <% } %>
    <h2>Pupitres</h2>
    <%
        for (Section section : sections) {
          boolean isChecked = fanfaronSectionIds.contains(section.getId());
    %>
    <div>
        <input type="checkbox" id="sectionId<%=section.getId()%>" name="sectionIds" value="<%= section.getId() %>" <%= isChecked ? "checked" : "" %> />
        <label for="sectionId<%=section.getId()%>"><%= section.getName() %></label>
    </div>
    <% } %>
    <button type="submit">Mettre à jour</button>
</form>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
