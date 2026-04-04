package controller;

import dto.MatchScore;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import service.MatchService;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/new-match")
public class NewMatchServlet extends HttpServlet {
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        this.matchService = (MatchService) getServletContext().getAttribute("matchService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/new_match.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String player1Name = req.getParameter("player1Name");
        String player2Name = req.getParameter("player2Name");

        if (player1Name.equals(player2Name)) { throw new IllegalArgumentException("Invalid names"); }

        MatchScore matchScore = matchService.createNewMatch(player1Name, player2Name);

        Map<UUID, MatchScore> ongoingMatches =
                (Map<UUID, MatchScore>) getServletContext().getAttribute("ongoingMatches");
        ongoingMatches.put(matchScore.getId(), matchScore);

        resp.sendRedirect("/tennis_app/match-score?uuid="+matchScore.getId());
    }
}
