package com.zipcode.soccerapp.repository;

import com.zipcode.soccerapp.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByClubId(Long clubId);
    boolean existsByIdAndClubId(Long id, Long clubId);
}
