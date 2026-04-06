CREATE TABLE IF NOT EXISTS players (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS matches (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    player1_id   INT NOT NULL,
    player2_id   INT NOT NULL,
    winner_id    INT,
    FOREIGN KEY (player1_id) REFERENCES players(id),
    FOREIGN KEY (player2_id) REFERENCES players(id)
);

INSERT INTO Players (name) VALUES ('Alice');
INSERT INTO Players (name) VALUES ('Andrew');
INSERT INTO Players (name) VALUES ('Johnny');
INSERT INTO Players (name) VALUES ('Bob');