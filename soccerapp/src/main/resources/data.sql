-- ============================================================
-- Soccer App Synthetic Data
-- Order: leagues first, then clubs, then players (FK order)
-- ============================================================

-- LEAGUES
INSERT INTO leagues (id, name, country, founded_year, commissioner) VALUES
(1, 'Premier League',  'England', 1992, 'Richard Masters'),
(2, 'La Liga',         'Spain',   1929, 'Javier Tebas'),
(3, 'Bundesliga',      'Germany', 1963, 'Hans-Joachim Watzke');

-- CLUBS (2 per league)
INSERT INTO clubs (id, league_id, name, city, stadium_name, founded_year, coach_name) VALUES
(1, 1, 'Manchester United', 'Manchester',  'Old Trafford',           1878, 'Ruben Amorim'),
(2, 1, 'Arsenal',          'London',      'Emirates Stadium',       1886, 'Mikel Arteta'),
(3, 2, 'Real Madrid',      'Madrid',      'Santiago Bernabeu',      1902, 'Carlo Ancelotti'),
(4, 2, 'FC Barcelona',     'Barcelona',   'Camp Nou',               1899, 'Hansi Flick'),
(5, 3, 'Bayern Munich',    'Munich',      'Allianz Arena',          1900, 'Vincent Kompany'),
(6, 3, 'Borussia Dortmund','Dortmund',    'Signal Iduna Park',      1909, 'Niko Kovac');

-- PLAYERS (2 per club)
INSERT INTO players (id, club_id, name, position, jersey_number, nationality, age) VALUES
-- Manchester United
(1,  1, 'Bruno Fernandes',   'Midfielder',       8, 'Portuguese', 30),
(2,  1, 'Rasmus Hojlund',    'Striker',         11, 'Danish',     22),
-- Arsenal
(3,  2, 'Bukayo Saka',       'Winger',           7, 'English',    23),
(4,  2, 'Martin Odegaard',   'Midfielder',        8, 'Norwegian', 26),
-- Real Madrid
(5,  3, 'Vinicius Junior',   'Winger',           7, 'Brazilian',  24),
(6,  3, 'Jude Bellingham',   'Midfielder',        5, 'English',   21),
-- FC Barcelona
(7,  4, 'Pedri',             'Midfielder',        8, 'Spanish',   22),
(8,  4, 'Robert Lewandowski','Striker',           9, 'Polish',    36),
-- Bayern Munich
(9,  5, 'Harry Kane',        'Striker',           9, 'English',   31),
(10, 5, 'Jamal Musiala',     'Midfielder',       42, 'German',    21),
-- Borussia Dortmund
(11, 6, 'Karim Adeyemi',     'Winger',           27, 'German',   23),
(12, 6, 'Emre Can',          'Midfielder',        23, 'German',  30);
