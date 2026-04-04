<%@ page import="java.util.UUID" %>
<%@ page import="dto.MatchScore" %>
<%@ page import="util.DisplayFormat" %><%--
  Created by IntelliJ IDEA.
  User: cypopik
  Date: 4/2/26
  Time: 10:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Match score</title>

    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<%
    MatchScore m = (MatchScore) request.getAttribute("matchScore");
    int player1Points = m.getPlayer1Score().getCurrentScore();
    int player2Points = m.getPlayer2Score().getCurrentScore();
    String winnerName = request.getAttribute("winnerName") != null ? request.getAttribute("winnerName").toString() : null;
%>

<table>
    <caption>Current match</caption>
    <thead>
    <tr>
        <th>Player 1</th>
        <th>Player 2</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>
            <p>
                <%= m.getPlayer1().getName() %>
            </p>
        </td>
        <td>
            <p>
                <%= m.getPlayer2().getName()  %>
            </p>
        </td>
    </tr>
    <tr>
        <td>
            <p>
                <%= m.getPlayer1Score().getSetsWon() %>
            </p>
        </td>
        <td>
            <p>
                <%= m.getPlayer2Score().getSetsWon()%>
            </p>
        </td>
    </tr>
    <% if (winnerName == null) { %>
    <tr>
        <td>
            <p>
                <%= m.getPlayer1Score().getGamesWon() %>
            </p>
        </td>
        <td>
            <p>
                <%= m.getPlayer2Score().getGamesWon()%>
            </p>
        </td>
    </tr>
    <tr>
        <td>
            <p>
                <%= DisplayFormat.formatPoints(player1Points) %>
            </p>
        </td>
        <td>
            <p>
                <%= DisplayFormat.formatPoints(player2Points) %>
            </p>
        </td>
    </tr>
    <tr>
        <td>
            <form action="<%= request.getContextPath() %>/match-score?uuid=<%= request.getAttribute("matchId") %>" method="post">
                <input type="hidden" name="matchId" value="<%=request.getAttribute("matchId").toString()%>" >

                <button type="submit" name="winnerId" value="<%=m.getPlayer1().getId()%>">
                    First player won
                </button>
            </form>
        </td>
        <td>
            <form action="<%= request.getContextPath() %>/match-score?uuid=<%= request.getAttribute("matchId") %>" method="post">
                <input type="hidden" name="matchId" value="<%=request.getAttribute("matchId").toString()%>">

                <button type="submit" name="winnerId" value="<%=m.getPlayer2().getId()%>">
                    Second player won
                </button>
            </form>
        </td>
    </tr>
    <% } %>
    </tbody>
</table>


<% if (winnerName != null) { %>
<div style="color: green;">
    <%= "Congratulation to " + winnerName + ". You won!"%>
</div>
<% } %>

<p><a href="<%= request.getContextPath() %>/">Return to main page</a></p>
<p><a href="<%= request.getContextPath() %>/new-match">Create new match</a></p>

</body>
</html>
