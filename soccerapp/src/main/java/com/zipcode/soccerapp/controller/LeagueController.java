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
}
