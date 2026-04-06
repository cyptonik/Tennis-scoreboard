package org.tennis.scoreboard.service;

import org.tennis.scoreboard.dto.MatchScore;
import org.tennis.scoreboard.dto.PlayerDto;
import org.tennis.scoreboard.dto.PlayerScore;

public class OngoingMatchService {
    private OngoingMatchService() { }

    public static void incrementWinnerScore(MatchScore matchScore, Long winnerId) {
        PlayerDto player1 = matchScore.getPlayer1();
        PlayerDto player2 = matchScore.getPlayer2();

        PlayerScore player1Score = matchScore.getPlayer1Score();
        PlayerScore player2Score = matchScore.getPlayer2Score();

        if (winnerId.equals(player1.getId())) {
            processPointWon(player1Score, player2Score);
        } else if (winnerId.equals(player2.getId())) {
            processPointWon(player2Score, player1Score);
        }
    }

    public static PlayerDto determineWinner(MatchScore matchScore) {
        if (matchScore.getPlayer1Score().getSetsWon() > matchScore.getPlayer2Score().getSetsWon()) {
            return matchScore.getPlayer1();
        } else {
            return matchScore.getPlayer2();
        }
    }

    public static void processPointWon(PlayerScore winningPlayerScore, PlayerScore losingPlayerScore) {
        winningPlayerScore.increaseCurrentScore();

        int winningPoints = winningPlayerScore.getCurrentScore();
        int losingPoints = losingPlayerScore.getCurrentScore();

        if (isGameWon(winningPoints, losingPoints)) {
            winningPlayerScore.resetCurrentScore();
            losingPlayerScore.resetCurrentScore();

            winningPlayerScore.increaseGamesWon();

            int winningGames = winningPlayerScore.getGamesWon();
            int losingGames = losingPlayerScore.getGamesWon();

            if (isSetWon(winningGames, losingGames) || isTiebreakWon(winningGames, losingGames)) {
                winningPlayerScore.resetGamesWon();
                losingPlayerScore.resetGamesWon();

                winningPlayerScore.increaseSetsWon();
            }
        }
    }

    private static boolean isGameWon(int winnerPoints, int loserPoints) {
        return winnerPoints >= 4 && winnerPoints - loserPoints >= 2;
    }

    private static boolean isSetWon(int winnerGames, int loserGames) {
        return winnerGames >= 6 && winnerGames - loserGames >= 2;
    }

    private static boolean isTiebreakWon(int winnerGames, int loserGames) {
        return winnerGames == 7 && loserGames == 6;
    }
}
