<%@ page import="dto.MatchDto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: cypopik
  Date: 4/3/26
  Time: 3:29 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Played matches</title>

    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>

<%
    List<MatchDto> finishedMatches = (List<MatchDto>) request.getAttribute("finishedMatches");
    int currentPage = Integer.parseInt(request.getAttribute("currentPage").toString());
    int totalPages =  Integer.parseInt(request.getAttribute("totalPages").toString());
    String filterName = request.getParameter("filter_by_player_name");

    String prevUrl = request.getContextPath() + "/matches?page=" + (currentPage - 1);
    String nextUrl = request.getContextPath() + "/matches?page=" + (currentPage + 1);

    if (filterName != null && !filterName.isEmpty()) {
        prevUrl += "&filter_by_player_name=" + filterName;
        nextUrl += "&filter_by_player_name=" + filterName;
    }
%>

<% if (!finishedMatches.isEmpty()) { %>
<table>
    <caption>Played matches</caption>
    <thead>
    <tr>
        <th>ID</th>
        <th>Player 1</th>
        <th>Player 2</th>
        <th>Winner</th>
    </tr>
    </thead>
    <tbody>
        <% for (MatchDto match : finishedMatches) { %>
    <tr>
        <%--
        <% if (filterName != null && (
                filterName.equals(match.getPlayer1().getName()) ||
                filterName.equals(match.getPlayer2().getName())
        )) {
        %>
        --%>
            <td><%= match.getId()%></td>
            <td><%= match.getPlayer1().getName() %></td>
            <td><%= match.getPlayer2().getName() %></td>
            <td><%= match.getWinner().getName() %></td>
        <%--
        <% } %>
        --%>
    </tr>
        <% } %>
</table>
<div class="pagination">
    <% if (totalPages > 1) {
        if (currentPage > 1) { %>
            <a href="<%= prevUrl %>">&lt; Prev</a>
        <% }
        final String url = request.getContextPath() + "/matches?page=";
        for (int i = 1; i <= totalPages; i++) {
            String currentUrl = url + String.valueOf(i);
            if (filterName != null && !filterName.isEmpty()) {
                currentUrl = currentUrl + "&filter_by_player_name=" + filterName;
            }
            if (i == currentPage) { %>
                <strong><%= i %></strong>
            <% } else { %>
                <a href=<%=currentUrl%>><%= i %></a>
            <% }
        } if (currentPage < totalPages) { %>
            <a href="<%= nextUrl %>">Next &gt;</a>
        <% } %>
    <% } %>
</div>
<% } else { %>
<p>No finished matches were found</p>
<% } %>

<p>
    <form action="<%= request.getContextPath() %>/matches" method="get">
        <input type="hidden" name="page" value="<%=currentPage%>">

        <label for="playerName">Filter by player name:</label>
        <input type="text" id="playerName" name="filter_by_player_name">
    </form>
</p>
<a href="<%= request.getContextPath() %>/">Return to main page</a>

</body>
</html>
