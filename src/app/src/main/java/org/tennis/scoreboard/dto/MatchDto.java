package org.tennis.scoreboard.dto;

public class MatchDto {
    private final Long id;
    private final PlayerDto player1;
    private final PlayerDto player2;
    private final PlayerDto winner;

    public MatchDto(Long id, PlayerDto player1, PlayerDto player2, PlayerDto winner) {
        this.id = id;
        this.player1 = player1;
        this.player2 = player2;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public PlayerDto getPlayer1() {
        return player1;
    }

    public PlayerDto getPlayer2() {
        return player2;
    }

    public PlayerDto getWinner() {
        return winner;
    }
}
