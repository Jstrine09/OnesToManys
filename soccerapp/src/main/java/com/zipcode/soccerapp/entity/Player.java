package com.zipcode.soccerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    private String name;
    private String position;
    private Integer jerseyNumber;
    private String nationality;
    private Integer age;

    public Player() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
