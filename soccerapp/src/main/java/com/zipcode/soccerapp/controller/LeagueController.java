package com.zipcode.soccerapp.controller;

import com.zipcode.soccerapp.entity.League;
import com.zipcode.soccerapp.repository.LeagueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueRepository leagueRepository;

    public LeagueController(LeagueRepository leagueRepository) {
        this.leagueRepository = leagueRepository;
    }

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
}
