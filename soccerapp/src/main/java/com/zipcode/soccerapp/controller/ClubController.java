package com.zipcode.soccerapp.controller;

import com.zipcode.soccerapp.entity.Club;
import com.zipcode.soccerapp.entity.League;
import com.zipcode.soccerapp.repository.ClubRepository;
import com.zipcode.soccerapp.repository.LeagueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubRepository clubRepository;
    private final LeagueRepository leagueRepository;

    public ClubController(ClubRepository clubRepository, LeagueRepository leagueRepository) {
        this.clubRepository = clubRepository;
        this.leagueRepository = leagueRepository;
    }

    // GET all clubs
    @GetMapping
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    // GET a single club by id
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable Long id) {
        return clubRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create a new club (requires leagueId in the request body as {"leagueId": 1, ...})
    @PostMapping
    public ResponseEntity<Club> createClub(@RequestBody ClubRequest request) {
        return leagueRepository.findById(request.leagueId())
                .map(league -> {
                    Club club = new Club();
                    club.setLeague(league);
                    club.setName(request.name());
                    club.setCity(request.city());
                    club.setStadiumName(request.stadiumName());
                    club.setFoundedYear(request.foundedYear());
                    club.setCoachName(request.coachName());
                    return ResponseEntity.ok(clubRepository.save(club));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT update an existing club
    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable Long id, @RequestBody ClubRequest request) {
        return clubRepository.findById(id)
                .map(club -> {
                    leagueRepository.findById(request.leagueId()).ifPresent(club::setLeague);
                    club.setName(request.name());
                    club.setCity(request.city());
                    club.setStadiumName(request.stadiumName());
                    club.setFoundedYear(request.foundedYear());
                    club.setCoachName(request.coachName());
                    return ResponseEntity.ok(clubRepository.save(club));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a club
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        if (!clubRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clubRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Request record for creating/updating a club
    record ClubRequest(Long leagueId, String name, String city, String stadiumName,
                       Integer foundedYear, String coachName) {}
}
