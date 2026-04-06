package org.tennis.scoreboard.util;

import org.tennis.scoreboard.dto.PlayerDto;
import org.tennis.scoreboard.model.Player;

public class PlayerMapper {
    public static Player toPlayer(PlayerDto player) {
        return new Player(
                player.getId(),
                player.getName()
        );
    }

    public static PlayerDto toDto(Player player) {
        return new PlayerDto(
                player.getId(),
                player.getName()
        );
    }
}
