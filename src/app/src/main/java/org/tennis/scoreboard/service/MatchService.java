package org.tennis.scoreboard.service;

import org.tennis.scoreboard.dto.MatchDto;
import org.tennis.scoreboard.dto.MatchScore;
import org.tennis.scoreboard.dto.PlayerDto;
import org.tennis.scoreboard.exception.PlayerNotFoundException;
import org.tennis.scoreboard.model.Match;
import org.tennis.scoreboard.model.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.tennis.scoreboard.util.MatchMapper;
import org.tennis.scoreboard.util.PlayerMapper;

import java.util.List;
import java.util.UUID;

public class MatchService {
    private final SessionFactory sessionFactory;

    public MatchService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public MatchScore createNewMatch(String player1Name, String player2Name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            UUID matchId = UUID.randomUUID();
            MatchScore matchScore = new MatchScore(matchId,
                    getPlayerByName(session, player1Name),
                    getPlayerByName(session, player2Name)
            );
            session.getTransaction().commit();

            return matchScore;
        }
    }

    public PlayerDto addFinishedMatch(MatchScore matchScore) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PlayerDto winnerDto = OngoingMatchService.determineWinner(matchScore);

            Player player1 = PlayerMapper.toPlayer(matchScore.getPlayer1());
            Player player2 = PlayerMapper.toPlayer(matchScore.getPlayer2());
            Player winner = PlayerMapper.toPlayer(winnerDto);

            Match match = new Match(player1, player2, winner);

            session.persist(match);
            session.getTransaction().commit();

            return winnerDto;
        }
    }

    public List<MatchDto> getFinishedMatches(String filterName) {
        if (filterName == null || filterName.isBlank()) {
            return getAllFinishedMatches();
        } else {
            return getFinishedMatchesByPlayer(filterName);
        }
    }

    public List<MatchDto> getAllFinishedMatches() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Match", Match.class).list()
                    .stream()
                            .map(MatchMapper::toDto)
                                    .toList();
        }
    }

    public List<MatchDto> getFinishedMatchesByPlayer(String filterName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Match m WHERE m.player1.name = :name OR m.player2.name = :name",
                            Match.class).setParameter("name", filterName)
                    .list()
                    .stream()
                    .map(MatchMapper::toDto)
                    .toList();
        }
    }

    public PlayerDto getPlayerByName(Session session, String name) {
        Player player = session.createQuery("FROM Player WHERE name = :name", Player.class)
                .setParameter("name", name)
                .uniqueResult();

        if (player == null) {
            throw new PlayerNotFoundException("Player '" + name + "' not found");
        }

        return new PlayerDto(player.getId(), player.getName());
    }
}
