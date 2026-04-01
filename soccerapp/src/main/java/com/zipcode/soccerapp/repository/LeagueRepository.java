package com.zipcode.soccerapp.repository;

import com.zipcode.soccerapp.entity.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueRepository extends JpaRepository<League, Long> {
}
