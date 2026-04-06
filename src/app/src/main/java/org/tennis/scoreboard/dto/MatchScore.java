package org.tennis.scoreboard.dto;

import java.util.UUID;

public class MatchScore {
    private final UUID id;
    private final PlayerDto player1;
    private final PlayerDto player2;
    private final PlayerScore player1Score;
    private final PlayerScore player2Score;

    public MatchScore(UUID id, PlayerDto player1, PlayerDto player2) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Score = new PlayerScore();
        this.player2Score = new PlayerScore();
    }

    public UUID getId() {
        return id;
    }

    public PlayerDto getPlayer1() {
        return player1;
    }

    public PlayerDto getPlayer2() {
        return player2;
    }

    public PlayerScore getPlayer1Score() {
        return player1Score;
    }

    public PlayerScore getPlayer2Score() {
        return player2Score;
    }
}
