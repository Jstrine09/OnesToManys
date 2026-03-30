package com.zipcode.soccerapp.repository;

import com.zipcode.soccerapp.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
