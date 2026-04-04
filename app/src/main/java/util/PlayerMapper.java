package util;

import dto.PlayerDto;
import model.Player;

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
