package com.zipcode.soccerapp.controller;

import com.zipcode.soccerapp.entity.Player;
import com.zipcode.soccerapp.repository.ClubRepository;
import com.zipcode.soccerapp.repository.PlayerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leagues/{leagueId}/clubs/{clubId}/players")
public class PlayerController {

    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;

    public PlayerController(PlayerRepository playerRepository, ClubRepository clubRepository) {
        this.playerRepository = playerRepository;
        this.clubRepository = clubRepository;
    }

    record PlayerRequest(@NotBlank @Size(max = 100) String name,
                         @Size(max = 50) String position,
                         Integer jerseyNumber,
                         @Size(max = 100) String nationality,
                         Integer age) {}

    // GET all players in a club
    @GetMapping
    public ResponseEntity<List<Player>> getPlayers(@PathVariable Long leagueId,
                                                   @PathVariable Long clubId) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playerRepository.findByClubId(clubId));
    }

    // GET one player in a club
    @GetMapping("/{playerId}")
    public ResponseEntity<Player> getPlayer(@PathVariable Long leagueId,
                                            @PathVariable Long clubId,
                                            @PathVariable Long playerId) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        if (!playerRepository.existsByIdAndClubId(playerId, clubId)) {
            return ResponseEntity.notFound().build();
        }
        return playerRepository.findById(playerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create a player in a club
    @PostMapping
    public ResponseEntity<Player> createPlayer(@PathVariable Long leagueId,
                                               @PathVariable Long clubId,
                                               @Valid @RequestBody PlayerRequest body) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        return clubRepository.findById(clubId)
                .map(club -> {
                    Player player = new Player();
                    player.setClub(club);
                    player.setName(body.name());
                    player.setPosition(body.position());
                    player.setJerseyNumber(body.jerseyNumber());
                    player.setNationality(body.nationality());
                    player.setAge(body.age());
                    return ResponseEntity.ok(playerRepository.save(player));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT update a player in a club
    @PutMapping("/{playerId}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long leagueId,
                                               @PathVariable Long clubId,
                                               @PathVariable Long playerId,
                                               @Valid @RequestBody PlayerRequest body) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        if (!playerRepository.existsByIdAndClubId(playerId, clubId)) {
            return ResponseEntity.notFound().build();
        }
        return playerRepository.findById(playerId)
                .map(player -> {
                    player.setName(body.name());
                    player.setPosition(body.position());
                    player.setJerseyNumber(body.jerseyNumber());
                    player.setNationality(body.nationality());
                    player.setAge(body.age());
                    return ResponseEntity.ok(playerRepository.save(player));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a player from a club
    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long leagueId,
                                             @PathVariable Long clubId,
                                             @PathVariable Long playerId) {
        if (!clubRepository.existsByIdAndLeagueId(clubId, leagueId)) {
            return ResponseEntity.notFound().build();
        }
        if (!playerRepository.existsByIdAndClubId(playerId, clubId)) {
            return ResponseEntity.notFound().build();
        }
        playerRepository.deleteById(playerId);
        return ResponseEntity.noContent().build();
    }
}
