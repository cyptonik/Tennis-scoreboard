<%--
  Created by IntelliJ IDEA.
  User: cypopik
  Date: 4/3/26
  Time: 4:55 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tennis scoreboard</title>
</head>
<body>
<p>Match control panel</p>
<ul>
    <li><a href="<%= request.getContextPath() %>/new-match">Create a new match</a></li>
    <li><a href="<%= request.getContextPath() %>/matches">Played matches</a></li>
</ul>

<% if (session.getAttribute("errorMessage") != null) { %>
<div style="color: red;">
    <%= session.getAttribute("errorMessage") %>
    <%session.removeAttribute("errorMessage");%>
</div>
<% } %>

</body>
</html>
