<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 09/05/2026
  Time: 09:20
  To change this template use File | Settings | File Templates.
--%>
<%
    String error = (String) request.getAttribute("error");
    String errorParameter = request.getParameter("error");
    String success = (String) request.getAttribute("success");
    String successParameter = request.getParameter("success");
    if (error != null) {
%>
<div>
    <p style="color: red;"><%= error %></p>
</div>
<%    } else if (errorParameter != null) {
%>
<div>
    <p style="color: red;"><%= errorParameter %></p>
</div>
<%
    }
    if (success != null) {
%>
<div>
    <p style="color: green;"><%= success %></p>
</div>
<%
    }
    else if (successParameter != null) {
%>
<div>
    <p style="color: green;"><%= successParameter %></p>
</div>
<%
    }
%>