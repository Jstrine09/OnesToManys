-- ============================================================
-- Soccer App Schema
-- Hierarchy: League -> Clubs -> Players
-- ============================================================

CREATE TABLE IF NOT EXISTS leagues (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    country       VARCHAR(100) NOT NULL,
    founded_year  INT,
    commissioner  VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS clubs (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    league_id     INT NOT NULL,
    name          VARCHAR(100) NOT NULL,
    city          VARCHAR(100),
    stadium_name  VARCHAR(150),
    founded_year  INT,
    coach_name    VARCHAR(100),
    FOREIGN KEY (league_id) REFERENCES leagues(id)
);

CREATE TABLE IF NOT EXISTS players (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    club_id        INT NOT NULL,
    name           VARCHAR(100) NOT NULL,
    position       VARCHAR(50),
    jersey_number  INT,
    nationality    VARCHAR(100),
    age            INT,
    FOREIGN KEY (club_id) REFERENCES clubs(id)
);
