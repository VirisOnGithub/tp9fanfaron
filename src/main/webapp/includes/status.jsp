<%--
  Created by IntelliJ IDEA.
  User: clement
  Date: 09/05/2026
  Time: 09:20
  To change this template use File | Settings | File Templates.
--%>
<%
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    if (error != null) {
%>
<div>
    <p style="color: red;"><%= error %></p>
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
%>