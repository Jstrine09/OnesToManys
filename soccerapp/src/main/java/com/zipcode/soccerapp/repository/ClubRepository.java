package com.zipcode.soccerapp.repository;

import com.zipcode.soccerapp.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Long> {
}
