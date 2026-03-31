CREATE TABLE IF NOT EXISTS players (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS matches (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    player1   INT NOT NULL,
    player2   INT NOT NULL,
    winner    INT,
    FOREIGN KEY (player1) REFERENCES players(id),
    FOREIGN KEY (player2) REFERENCES players(id)
);