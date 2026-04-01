package com.zipcode.soccerapp.config;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.zipcode.soccerapp.entity.Club;
import com.zipcode.soccerapp.entity.League;
import com.zipcode.soccerapp.entity.Player;
import com.zipcode.soccerapp.repository.ClubRepository;
import com.zipcode.soccerapp.repository.LeagueRepository;
import com.zipcode.soccerapp.repository.PlayerRepository;
import com.zipcode.soccerapp.service.FootballDataService;
import com.zipcode.soccerapp.service.FootballDataService.ApiPlayer;
import com.zipcode.soccerapp.service.FootballDataService.ApiTeam;
import com.zipcode.soccerapp.service.FootballDataService.ApiTeamsResponse;

@Component
public class DataLoader implements CommandLineRunner {

    // Competition IDs on football-data.org
    private static final int[] COMPETITION_IDS = {2021, 2014, 2002}; // PL, La Liga, Bundesliga

    private static final int CLUBS_PER_LEAGUE = 18;
    private static final int PLAYERS_PER_CLUB = 11;

    private final FootballDataService footballDataService;
    private final LeagueRepository leagueRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;

    public DataLoader(FootballDataService footballDataService,
                      LeagueRepository leagueRepository,
                      ClubRepository clubRepository,
                      PlayerRepository playerRepository) {
        this.footballDataService = footballDataService;
        this.leagueRepository = leagueRepository;
        this.clubRepository = clubRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // Skip seeding if data already exists
        if (leagueRepository.count() > 0) {
            System.out.println("[DataLoader] Database already seeded — skipping API fetch.");
            return;
        }

        System.out.println("[DataLoader] Seeding database from football-data.org...");

        for (int i = 0; i < COMPETITION_IDS.length; i++) {
            int competitionId = COMPETITION_IDS[i];

            System.out.println("[DataLoader] Fetching competition " + competitionId + "...");
            ApiTeamsResponse response = footballDataService.fetchTeamsByCompetition(competitionId);

            // Save the League
            // area may be absent in the simplified competition object returned by the teams endpoint
            String country = (response.competition().area() != null)
                    ? response.competition().area().name()
                    : switch (competitionId) {
                        case 2021 -> "England";
                        case 2014 -> "Spain";
                        case 2002 -> "Germany";
                        default -> "Unknown";
                    };

            League league = new League();
            league.setName(response.competition().name());
            league.setCountry(country);
            leagueRepository.save(league);
            System.out.println("[DataLoader]   Saved league: " + league.getName());

            // Save clubs (limited to CLUBS_PER_LEAGUE)
            List<ApiTeam> teams = response.teams();
            int teamLimit = Math.min(CLUBS_PER_LEAGUE, teams.size());

            for (int j = 0; j < teamLimit; j++) {
                ApiTeam apiTeam = teams.get(j);

                Club club = new Club();
                club.setLeague(league);
                club.setName(apiTeam.name());
                club.setStadiumName(apiTeam.venue());
                club.setFoundedYear(apiTeam.founded());
                if (apiTeam.coach() != null) {
                    club.setCoachName(apiTeam.coach().name());
                }
                clubRepository.save(club);
                System.out.println("[DataLoader]     Saved club: " + club.getName());

                // Save players from squad (limited to PLAYERS_PER_CLUB)
                if (apiTeam.squad() != null && !apiTeam.squad().isEmpty()) {
                    List<ApiPlayer> squad = apiTeam.squad();
                    int playerLimit = Math.min(PLAYERS_PER_CLUB, squad.size());

                    for (int k = 0; k < playerLimit; k++) {
                        ApiPlayer apiPlayer = squad.get(k);

                        Player player = new Player();
                        player.setClub(club);
                        player.setName(apiPlayer.name());
                        player.setPosition(apiPlayer.position());
                        player.setJerseyNumber(apiPlayer.shirtNumber());
                        player.setNationality(apiPlayer.nationality());

                        if (apiPlayer.dateOfBirth() != null) {
                            try {
                                LocalDate dob = LocalDate.parse(apiPlayer.dateOfBirth());
                                int age = (int) ChronoUnit.YEARS.between(dob, LocalDate.now());
                                player.setAge(age);
                            } catch (Exception e) {
                                // skip age if date format is unexpected
                            }
                        }
                        playerRepository.save(player);
                    }
                    System.out.println("[DataLoader]       Saved " + playerLimit + " players for " + club.getName());
                }
            }

            // Respect the 10 calls/minute rate limit between competition calls
            if (i < COMPETITION_IDS.length - 1) {
                System.out.println("[DataLoader] Waiting 6 seconds (rate limit)...");
                Thread.sleep(6200);
            }
        }

        System.out.println("[DataLoader] Done! Database seeded with real soccer data.");
    }
}
