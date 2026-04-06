import org.tennis.scoreboard.dto.PlayerScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.tennis.scoreboard.service.OngoingMatchService;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OngoingMatchService - полное покрытие")
class OngoingMatchServiceTest {

    private PlayerScore winner;
    private PlayerScore loser;

    @BeforeEach
    void init() {
        winner = new PlayerScore();
        loser  = new PlayerScore();
    }

    @Nested
    @DisplayName("Начисление очков внутри гейма")
    class PointScoring {

        @Test
        @DisplayName("0 -> 1 после первого очка")
        void firstPoint() {
            OngoingMatchService.processPointWon(winner, loser);
            assertEquals(1, winner.getCurrentScore());
            assertEquals(0, loser.getCurrentScore());
        }

        @Test
        @DisplayName("Счёт растёт линейно: 1 -> 2 -> 3")
        void scoreIncrementsLinearly() {
            scorePoints(winner, loser, 3);
            assertEquals(3, winner.getCurrentScore());
        }

        @Test
        @DisplayName("Проигравший не получает очко")
        void loserScoreUnchanged() {
            OngoingMatchService.processPointWon(winner, loser);
            assertEquals(0, loser.getCurrentScore());
        }
    }

    @Nested
    @DisplayName("Завершение гейма")
    class GameWinning {

        @Test
        @DisplayName("4:0 - гейм выигран, счёт сбрасывается")
        void cleanGame_4_0() {
            scorePoints(winner, loser, 4);

            assertEquals(0, winner.getCurrentScore(), "текущий счёт должен сброситься");
            assertEquals(0, loser.getCurrentScore());
            assertEquals(1, winner.getGamesWon());
            assertEquals(0, loser.getGamesWon());
        }

        @Test
        @DisplayName("4:1 - гейм выигран")
        void game_4_1() {
            scorePoints(loser, winner, 1);
            scorePoints(winner, loser, 4);

            assertEquals(1, winner.getGamesWon());
        }

        @Test
        @DisplayName("4:2 - гейм выигран")
        void game_4_2() {
            scorePoints(loser, winner, 2);
            scorePoints(winner, loser, 4);

            assertEquals(1, winner.getGamesWon());
        }

        @Test
        @DisplayName("3 очка - гейм ещё не выигран")
        void threePointsNotEnough() {
            scorePoints(winner, loser, 3);

            assertEquals(0, winner.getGamesWon());
            assertEquals(3, winner.getCurrentScore());
        }

        @Test
        @DisplayName("После выигрыша гейма текущий счёт обоих = 0")
        void scoresResetAfterGame() {
            scorePoints(winner, loser, 4);

            assertEquals(0, winner.getCurrentScore());
            assertEquals(0, loser.getCurrentScore());
        }
    }

    @Nested
    @DisplayName("Deuce / advantage")
    class DeuceAndAdvantage {

        @Test
        @DisplayName("40:40 -> advantage (41:40), гейм не заканчивается")
        void deuceDoesNotEndGame() {
            winner.setCurrentScore(40);
            loser.setCurrentScore(40);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(41, winner.getCurrentScore());
            assertEquals(40, loser.getCurrentScore());
            assertEquals(0, winner.getGamesWon());
        }

        @Test
        @DisplayName("41:40 -> гейм выигран")
        void advantageWinsGame() {
            winner.setCurrentScore(41);
            loser.setCurrentScore(40);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(1, winner.getGamesWon());
            assertEquals(0, winner.getCurrentScore());
        }

        @Test
        @DisplayName("40:41 -> равенство (41:41), гейм не заканчивается")
        void advantageLostBecomesDeuce() {
            winner.setCurrentScore(40);
            loser.setCurrentScore(41);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(41, winner.getCurrentScore());
            assertEquals(41, loser.getCurrentScore());
            assertEquals(0, winner.getGamesWon());
        }

        @Test
        @DisplayName("41:41 -> 42:41, гейм не заканчивается")
        void doubleDeuce() {
            winner.setCurrentScore(41);
            loser.setCurrentScore(41);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(42, winner.getCurrentScore());
            assertEquals(0, winner.getGamesWon());
        }

        @Test
        @DisplayName("42:41 -> гейм выигран")
        void multipleDeuce_gameWon() {
            winner.setCurrentScore(42);
            loser.setCurrentScore(41);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(1, winner.getGamesWon());
        }

        @Test
        @DisplayName("3:3 -> advantage не наступает (нужно >= 4)")
        void noAdvantageAt3_3() {
            winner.setCurrentScore(3);
            loser.setCurrentScore(3);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(4, winner.getCurrentScore());
            assertEquals(0, winner.getGamesWon());
        }
    }

    @Nested
    @DisplayName("Завершение сета")
    class SetWinning {

        @Test
        @DisplayName("6:0 - сет выигран, геймы сбрасываются")
        void set_6_0() {
            playGames(winner, loser, 6);

            assertEquals(1, winner.getSetsWon());
            assertEquals(0, winner.getGamesWon(), "геймы должны сброситься");
            assertEquals(0, loser.getGamesWon());
        }

        @Test
        @DisplayName("6:4 - сет выигран")
        void set_6_4() {
            playGames(loser, winner, 4);
            playGames(winner, loser, 6);

            assertEquals(1, winner.getSetsWon());
        }

        @Test
        @DisplayName("6:5 - сет ещё не выигран")
        void set_6_5_notWon() {
            playGames(loser, winner, 5);
            playGames(winner, loser, 6);

            assertEquals(0, winner.getSetsWon());
            assertEquals(6, winner.getGamesWon());
        }

        @Test
        @DisplayName("7:5 - сет выигран")
        void set_7_5() {
            playGames(loser, winner, 5);
            playGames(winner, loser, 7);

            assertEquals(1, winner.getSetsWon());
        }

        @Test
        @DisplayName("5:6 - сет не выигран (недостаточно геймов)")
        void set_5_6_notWon() {
            playGames(winner, loser, 5);
            playGames(loser, winner, 6);

            assertEquals(0, winner.getSetsWon());
        }

        @Test
        @DisplayName("После выигрыша сета проигравший тоже теряет геймы")
        void loserGamesResetAfterSet() {
            playGames(loser, winner, 3);
            playGames(winner, loser, 6);

            assertEquals(0, loser.getGamesWon());
        }
    }

    @Nested
    @DisplayName("Тай-брейк")
    class Tiebreak {

        @Test
        @DisplayName("7:6 - сет выигран через тай-брейк")
        void tiebreak_7_6() {
            playGames(loser, winner, 6);
            playGames(winner, loser, 7);

            assertEquals(1, winner.getSetsWon());
        }

        @Test
        @DisplayName("6:6 - тай-брейк ещё не выигран")
        void tiebreak_6_6_notYet() {
            winner.setGamesWon(6);
            loser.setGamesWon(6);

            scorePoints(winner, loser, 4);

            assertEquals(1, winner.getSetsWon());
        }

        @Test
        @DisplayName("7:5 - это обычная победа в сете, не тай-брейк")
        void set_7_5_isNotTiebreak() {
            playGames(loser, winner, 5);
            playGames(winner, loser, 7);

            assertEquals(1, winner.getSetsWon());
        }
    }

    @Nested
    @DisplayName("Каскадные переходы")
    class CascadeTransitions {

        @Test
        @DisplayName("Очко завершает гейм, гейм завершает сет - всё сбрасывается")
        void pointTriggersGameAndSet() {
            playGames(winner, loser, 5);
            scorePoints(winner, loser, 3);

            OngoingMatchService.processPointWon(winner, loser);

            assertEquals(1, winner.getSetsWon());
            assertEquals(0, winner.getGamesWon());
            assertEquals(0, winner.getCurrentScore());
        }

        @Test
        @DisplayName("Два сета подряд - победа в матче")
        void twoSetsWon() {
            playGames(winner, loser, 6);
            playGames(winner, loser, 6);

            assertEquals(2, winner.getSetsWon());
        }
    }

    @Nested
    @DisplayName("PlayerScore - конструкторы и валидация")
    class PlayerScoreValidation {

        @Test
        @DisplayName("Конструктор по умолчанию - все поля 0")
        void defaultConstructor() {
            PlayerScore ps = new PlayerScore();
            assertEquals(0, ps.getCurrentScore());
            assertEquals(0, ps.getGamesWon());
            assertEquals(0, ps.getSetsWon());
        }

        @Test
        @DisplayName("Параметризованный конструктор задаёт поля")
        void paramConstructor() {
            PlayerScore ps = new PlayerScore(3, 4, 1);
            assertEquals(3, ps.getCurrentScore());
            assertEquals(4, ps.getGamesWon());
            assertEquals(1, ps.getSetsWon());
        }

        @Test
        @DisplayName("Отрицательный currentScore в конструкторе -> исключение")
        void negativeCurrentScoreThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new PlayerScore(-1, 0, 0));
        }

        @Test
        @DisplayName("Отрицательный gamesWon в конструкторе -> исключение")
        void negativeGamesWonThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new PlayerScore(0, -1, 0));
        }

        @Test
        @DisplayName("Отрицательный setsWon в конструкторе -> исключение")
        void negativeSetsWonThrows() {
            assertThrows(IllegalArgumentException.class,
                    () -> new PlayerScore(0, 0, -1));
        }

        @Test
        @DisplayName("setCurrentScore с отрицательным значением -> исключение")
        void setNegativeCurrentScore() {
            assertThrows(IllegalArgumentException.class,
                    () -> winner.setCurrentScore(-1));
        }

        @Test
        @DisplayName("setGamesWon с отрицательным значением -> исключение")
        void setNegativeGamesWon() {
            assertThrows(IllegalArgumentException.class,
                    () -> winner.setGamesWon(-5));
        }

        @Test
        @DisplayName("setSetsWon с отрицательным значением -> исключение")
        void setNegativeSetsWon() {
            assertThrows(IllegalArgumentException.class,
                    () -> winner.setSetsWon(-1));
        }

        @Test
        @DisplayName("increaseCurrentScore увеличивает счёт на 1")
        void increaseCurrentScore() {
            winner.increaseCurrentScore();
            assertEquals(1, winner.getCurrentScore());
        }

        @Test
        @DisplayName("increaseGamesWon увеличивает геймы на 1")
        void increaseGamesWon() {
            winner.increaseGamesWon();
            assertEquals(1, winner.getGamesWon());
        }

        @Test
        @DisplayName("increaseSetsWon увеличивает сеты на 1")
        void increaseSetsWon() {
            winner.increaseSetsWon();
            assertEquals(1, winner.getSetsWon());
        }

        @Test
        @DisplayName("resetCurrentScore обнуляет текущий счёт")
        void resetCurrentScore() {
            winner.setCurrentScore(3);
            winner.resetCurrentScore();
            assertEquals(0, winner.getCurrentScore());
        }

        @Test
        @DisplayName("resetGamesWon обнуляет геймы")
        void resetGamesWon() {
            winner.setGamesWon(4);
            winner.resetGamesWon();
            assertEquals(0, winner.getGamesWon());
        }
    }

    @Nested
    @DisplayName("Симметрия: player2 выигрывает")
    class SymmetryPlayer2 {

        @Test
        @DisplayName("Player2: 4 очка -> гейм")
        void player2WinsGame() {
            scorePoints(loser, winner, 4);
            assertEquals(1, loser.getGamesWon());
        }

        @Test
        @DisplayName("Player2: 6 геймов -> сет")
        void player2WinsSet() {
            playGames(loser, winner, 6);
            assertEquals(1, loser.getSetsWon());
        }

        @Test
        @DisplayName("Player2 тай-брейк 7:6")
        void player2WinsTiebreak() {
            playGames(winner, loser, 6);
            playGames(loser, winner, 7);
            assertEquals(1, loser.getSetsWon());
        }
    }

    /** Начисляет n очков игроку a (b — противник). */
    private void scorePoints(PlayerScore a, PlayerScore b, int n) {
        for (int i = 0; i < n; i++) {
            OngoingMatchService.processPointWon(a, b);
        }
    }

    /**
     * Разыгрывает n геймов подряд: a выигрывает каждый гейм 4:0.
     * b — противник, который ничего не получает.
     */
    private void playGames(PlayerScore a, PlayerScore b, int n) {
        for (int i = 0; i < n; i++) {
            scorePoints(a, b, 4);
        }
    }
}
