package com.zipcode.soccerapp.repository;

import com.zipcode.soccerapp.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByLeagueId(Long leagueId);
    boolean existsByIdAndLeagueId(Long id, Long leagueId);
}
