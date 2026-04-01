package com.zipcode.soccerapp.controller;

import com.zipcode.soccerapp.entity.Club;
import com.zipcode.soccerapp.entity.League;
import com.zipcode.soccerapp.entity.Player;
import com.zipcode.soccerapp.repository.ClubRepository;
import com.zipcode.soccerapp.repository.LeagueRepository;
import com.zipcode.soccerapp.repository.PlayerRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataExportController {

    private final LeagueRepository leagueRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;

    public DataExportController(LeagueRepository leagueRepository,
                                ClubRepository clubRepository,
                                PlayerRepository playerRepository) {
        this.leagueRepository = leagueRepository;
        this.clubRepository = clubRepository;
        this.playerRepository = playerRepository;
    }

    // ── Import DTOs ───────────────────────────────────────────────────────────

    record PlayerImport(String name, String position, Integer jerseyNumber,
                        String nationality, Integer age) {}

    record ClubImport(String name, String city, String stadiumName,
                      Integer foundedYear, String coachName,
                      List<PlayerImport> players) {}

    record LeagueImport(String name, String country, Integer foundedYear,
                        String commissioner, List<ClubImport> clubs) {}

    record ImportRequest(List<LeagueImport> leagues) {}

    // ── JSON Export ───────────────────────────────────────────────────────────

    @GetMapping("/export/json")
    @Transactional(readOnly = true)
    public ResponseEntity<ImportRequest> exportJson() {
        List<League> leagues = leagueRepository.findAll();

        // Build a plain DTO tree so lazy collections are fully resolved
        List<LeagueImport> leagueImports = leagues.stream().map(league -> {
            List<ClubImport> clubImports = league.getClubs().stream().map(club -> {
                List<PlayerImport> playerImports = club.getPlayers().stream().map(player ->
                    new PlayerImport(
                        player.getName(),
                        player.getPosition(),
                        player.getJerseyNumber(),
                        player.getNationality(),
                        player.getAge()
                    )
                ).toList();

                return new ClubImport(
                    club.getName(),
                    club.getCity(),
                    club.getStadiumName(),
                    club.getFoundedYear(),
                    club.getCoachName(),
                    playerImports
                );
            }).toList();

            return new LeagueImport(
                league.getName(),
                league.getCountry(),
                league.getFoundedYear(),
                league.getCommissioner(),
                clubImports
            );
        }).toList();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=soccerdata.json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ImportRequest(leagueImports));
    }

    // ── SQL Export ────────────────────────────────────────────────────────────

    @GetMapping("/export/sql")
    @Transactional(readOnly = true)
    public ResponseEntity<String> exportSql() {
        List<League> leagues = leagueRepository.findAll();

        StringBuilder sql = new StringBuilder();
        sql.append("-- Soccer App Data Export\n\n");
        sql.append("DELETE FROM players;\n");
        sql.append("DELETE FROM clubs;\n");
        sql.append("DELETE FROM leagues;\n\n");

        for (League league : leagues) {
            sql.append(String.format(
                "INSERT INTO leagues (id, name, country, founded_year, commissioner) VALUES (%d, %s, %s, %s, %s);\n",
                league.getId(),
                quote(league.getName()),
                quote(league.getCountry()),
                nullOrInt(league.getFoundedYear()),
                quote(league.getCommissioner())
            ));

            for (Club club : league.getClubs()) {
                sql.append(String.format(
                    "INSERT INTO clubs (id, league_id, name, city, stadium_name, founded_year, coach_name) VALUES (%d, %d, %s, %s, %s, %s, %s);\n",
                    club.getId(),
                    league.getId(),
                    quote(club.getName()),
                    quote(club.getCity()),
                    quote(club.getStadiumName()),
                    nullOrInt(club.getFoundedYear()),
                    quote(club.getCoachName())
                ));

                for (Player player : club.getPlayers()) {
                    sql.append(String.format(
                        "INSERT INTO players (id, club_id, name, position, jersey_number, nationality, age) VALUES (%d, %d, %s, %s, %s, %s, %s);\n",
                        player.getId(),
                        club.getId(),
                        quote(player.getName()),
                        quote(player.getPosition()),
                        nullOrInt(player.getJerseyNumber()),
                        quote(player.getNationality()),
                        nullOrInt(player.getAge())
                    ));
                }
            }
            sql.append("\n");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=soccerdata.sql")
                .contentType(MediaType.TEXT_PLAIN)
                .body(sql.toString());
    }

    // ── JSON Import ───────────────────────────────────────────────────────────

    @PostMapping("/import/json")
    @Transactional
    public ResponseEntity<String> importJson(@RequestBody ImportRequest request) {
        // Delete in FK order (players first, then clubs, then leagues)
        playerRepository.deleteAll();
        clubRepository.deleteAll();
        leagueRepository.deleteAll();

        for (LeagueImport li : request.leagues()) {
            League league = new League();
            league.setName(li.name());
            league.setCountry(li.country());
            league.setFoundedYear(li.foundedYear());
            league.setCommissioner(li.commissioner());
            leagueRepository.save(league);

            if (li.clubs() != null) {
                for (ClubImport ci : li.clubs()) {
                    Club club = new Club();
                    club.setLeague(league);
                    club.setName(ci.name());
                    club.setCity(ci.city());
                    club.setStadiumName(ci.stadiumName());
                    club.setFoundedYear(ci.foundedYear());
                    club.setCoachName(ci.coachName());
                    clubRepository.save(club);

                    if (ci.players() != null) {
                        for (PlayerImport pi : ci.players()) {
                            Player player = new Player();
                            player.setClub(club);
                            player.setName(pi.name());
                            player.setPosition(pi.position());
                            player.setJerseyNumber(pi.jerseyNumber());
                            player.setNationality(pi.nationality());
                            player.setAge(pi.age());
                            playerRepository.save(player);
                        }
                    }
                }
            }
        }

        return ResponseEntity.ok("Import successful — " + request.leagues().size() + " leagues loaded.");
    }

    // ── SQL Helpers ───────────────────────────────────────────────────────────

    private String quote(String value) {
        if (value == null) return "NULL";
        return "'" + value.replace("'", "''") + "'";
    }

    private String nullOrInt(Integer value) {
        return value == null ? "NULL" : value.toString();
    }
}
