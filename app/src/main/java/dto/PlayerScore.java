package dto;

public class PlayerScore {
    private int currentScore;
    private int gamesWon;
    private int setsWon;

    public PlayerScore() {
        this.currentScore = 0;
        this.gamesWon = 0;
        this.setsWon = 0;
    }

    public PlayerScore(int currentScore, int gamesWon, int setsWon) {
        if (currentScore < 0 || gamesWon < 0 || setsWon < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.currentScore = currentScore;
        this.gamesWon = gamesWon;
        this.setsWon = setsWon;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getSetsWon() {
        return setsWon;
    }

    public void setCurrentScore(int currentScore) {
        if (currentScore < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.currentScore = currentScore;
    }

    public void setGamesWon(int gamesWon) {
        if (gamesWon < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.gamesWon = gamesWon;
    }

    public void setSetsWon(int setsWon) {
        if (setsWon < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        this.setsWon = setsWon;
    }

    public void resetCurrentScore() {
        this.currentScore = 0;
    }

    public void resetGamesWon() {
        this.gamesWon = 0;
    }

    public void increaseCurrentScore() {
        this.currentScore++;
    }

    public void increaseGamesWon() {
        this.gamesWon++;
    }

    public void increaseSetsWon() {
        this.setsWon++;
    }
}
