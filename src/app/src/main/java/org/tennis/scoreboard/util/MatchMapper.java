package org.tennis.scoreboard.util;

import org.tennis.scoreboard.dto.MatchDto;
import org.tennis.scoreboard.model.Match;

public class MatchMapper {
    public static MatchDto toDto(Match match) {
        return new MatchDto(
                match.getId(),
                PlayerMapper.toDto(match.getPlayer1()),
                PlayerMapper.toDto(match.getPlayer2()),
                PlayerMapper.toDto(match.getWinner())
        );
    }

    public static Match toMatch(MatchDto match) {
        return new Match(
                match.getId(),
                PlayerMapper.toPlayer(match.getPlayer1()),
                PlayerMapper.toPlayer(match.getPlayer2()),
                PlayerMapper.toPlayer(match.getWinner())
        );
    }
}
