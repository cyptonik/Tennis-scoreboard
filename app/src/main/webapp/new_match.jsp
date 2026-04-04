<%--
  Created by IntelliJ IDEA.
  User: cypopik
  Date: 4/1/26
  Time: 11:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create a new match</title>
</head>
<body>
<% if (session.getAttribute("errorMessage") != null) { %>
<div style="color: red;">
    <%=session.getAttribute("errorMessage")%>
    <%session.removeAttribute("errorMessage");%>
</div>
<% } %>

<form action="<%= request.getContextPath() %>/new-match" method="post">
    <label for="player1">First player's name:</label>
    <input type="text" id="player1" name="player1Name" required>

    <label for="player2">Second player's name:</label>
    <input type="text" id="player2" name="player2Name" required>

    <input type="submit" value="Begin">
</form>

<a href="<%= request.getContextPath() %>/">Return to main page</a>

</body>
</html>
