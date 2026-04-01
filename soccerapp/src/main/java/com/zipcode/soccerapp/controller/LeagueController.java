package com.zipcode.soccerapp.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipcode.soccerapp.entity.Club;
import com.zipcode.soccerapp.entity.League;
import com.zipcode.soccerapp.repository.ClubRepository;
import com.zipcode.soccerapp.repository.LeagueRepository;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueRepository leagueRepository;
    private final ClubRepository clubRepository;

    public LeagueController(LeagueRepository leagueRepository, ClubRepository clubRepository) {
        this.leagueRepository = leagueRepository;
        this.clubRepository = clubRepository;
    }

    // ── League CRUD ──────────────────────────────────────────────────────────

    // GET all leagues
    @GetMapping
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    // GET a single league by id
    @GetMapping("/{id}")
    public ResponseEntity<League> getLeagueById(@PathVariable Long id) {
        return leagueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create a new league
    @PostMapping
    public League createLeague(@RequestBody League league) {
        return leagueRepository.save(league);
    }

    // PUT update an existing league
    @PutMapping("/{id}")
    public ResponseEntity<League> updateLeague(@PathVariable Long id, @RequestBody League updated) {
        return leagueRepository.findById(id)
                .map(league -> {
                    league.setName(updated.getName());
                    league.setCountry(updated.getCountry());
                    league.setFoundedYear(updated.getFoundedYear());
                    league.setCommissioner(updated.getCommissioner());
                    return ResponseEntity.ok(leagueRepository.save(league));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a league
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeague(@PathVariable Long id) {
        if (!leagueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        leagueRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Nested Club endpoints (/api/leagues/{leagueId}/clubs) ─────────────────

    record ClubRequest(@NotBlank @Size(max = 100) String name,
                       @Size(max = 150) String city,
                       @Size(max = 150) String stadiumName,
                       Integer foundedYear,
                       @Size(max = 100) String coachName) {}

    // GET all clubs in a league
    @GetMapping("/{leagueId}/clubs")
    public ResponseEntity<List<Club>> getClubsByLeague(@PathVariable Long leagueId) {
        if (!leagueRepository.existsById(leagueId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clubRepository.findByLeagueId(leagueId));
    }

    // GET one club inside a league
    @GetMapping("/{leagueId}/clubs/{clubId}")
    public ResponseEntity<Club> getClubInLeague(@PathVariable Long leagueId,
                                                @PathVariable Long clubId) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        return clubRepository.findById(clubId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create a club under a league
    @PostMapping("/{leagueId}/clubs")
    public ResponseEntity<Club> createClubInLeague(@PathVariable Long leagueId,
                                                   @Valid @RequestBody ClubRequest body) {
        return leagueRepository.findById(leagueId)
                .map(league -> {
                    Club club = new Club();
                    club.setLeague(league);
                    club.setName(body.name());
                    club.setCity(body.city());
                    club.setStadiumName(body.stadiumName());
                    club.setFoundedYear(body.foundedYear());
                    club.setCoachName(body.coachName());
                    return ResponseEntity.ok(clubRepository.save(club));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT update a club inside a league
    @PutMapping("/{leagueId}/clubs/{clubId}")
    public ResponseEntity<Club> updateClubInLeague(@PathVariable Long leagueId,
                                                   @PathVariable Long clubId,
                                                   @Valid @RequestBody ClubRequest body) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        return clubRepository.findById(clubId)
                .map(club -> {
                    club.setName(body.name());
                    club.setCity(body.city());
                    club.setStadiumName(body.stadiumName());
                    club.setFoundedYear(body.foundedYear());
                    club.setCoachName(body.coachName());
                    return ResponseEntity.ok(clubRepository.save(club));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a club from a league
    @DeleteMapping("/{leagueId}/clubs/{clubId}")
    public ResponseEntity<Void> deleteClubFromLeague(@PathVariable Long leagueId,
                                                     @PathVariable Long clubId) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        clubRepository.deleteById(clubId);
        return ResponseEntity.noContent().build();
    }
}
