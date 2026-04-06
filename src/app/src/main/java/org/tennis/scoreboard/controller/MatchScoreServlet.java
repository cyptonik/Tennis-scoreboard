package org.tennis.scoreboard.controller;

import org.tennis.scoreboard.dto.MatchScore;
import org.tennis.scoreboard.dto.PlayerDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;
import org.tennis.scoreboard.service.MatchService;
import org.tennis.scoreboard.service.OngoingMatchService;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {
    private Map<UUID, MatchScore> ongoingMatches;
    private MatchService matchService;

    @Override
    public void init() throws ServletException {
        this.ongoingMatches = (Map<UUID, MatchScore>) getServletContext().getAttribute("ongoingMatches");
        this.matchService = new MatchService(
                (SessionFactory) getServletContext().getAttribute("sessionFactory")
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        renderMatchScore(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = getMatchId(req);
        Long winnerId = Long.valueOf(req.getParameter("winnerId"));

        MatchScore matchScore = ongoingMatches.get(id);
        OngoingMatchService.incrementWinnerScore(matchScore, winnerId);

        if (isMatchEnded(matchScore)) {
            PlayerDto winner = matchService.addFinishedMatch(matchScore);
            ongoingMatches.remove(id);
            req.setAttribute("winnerName", winner.getName());
            req.setAttribute("matchScore", matchScore);
            req.getRequestDispatcher("/match_score.jsp").forward(req, resp);
        } else {
            renderMatchScore(req, resp);
        }
    }

    private boolean isMatchEnded(MatchScore matchScore) {
        return matchScore.getPlayer1Score().getSetsWon() == 2
                || matchScore.getPlayer2Score().getSetsWon() == 2;
    }

    private void renderMatchScore(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UUID id = getMatchId(req);
        MatchScore matchScore = ongoingMatches.get(id);
        if (matchScore == null) {
            throw new IllegalArgumentException("Active matches with this UUID do not exist");
        }

        req.setAttribute("matchScore", ongoingMatches.get(id));
        req.setAttribute("matchId", id.toString());

        req.getRequestDispatcher("/match_score.jsp").forward(req, resp);
    }

    private UUID getMatchId(HttpServletRequest req) {
        String uuidStr = req.getParameter("uuid");
        if (uuidStr == null) { throw new IllegalArgumentException("Parameter uuid not specified"); }

        return UUID.fromString(uuidStr);
    }
}
