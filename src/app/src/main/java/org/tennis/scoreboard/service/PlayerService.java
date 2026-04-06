package org.tennis.scoreboard.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.tennis.scoreboard.dto.PlayerDto;
import org.tennis.scoreboard.model.Player;
import org.tennis.scoreboard.util.PlayerMapper;

import java.util.List;

public class PlayerService {
    private final SessionFactory sessionFactory;

    public PlayerService(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<PlayerDto> getAllPlayers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Player", Player.class).list()
                    .stream()
                    .map(PlayerMapper::toDto)
                    .toList();
        }
    }
}
