package com.zipcode.soccerapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "clubs")
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "league_id", nullable = false)
    private League league;

    private String name;
    private String city;
    private String stadiumName;
    private Integer foundedYear;
    private String coachName;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Player> players;

    public Club() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public League getLeague() { return league; }
    public void setLeague(League league) { this.league = league; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStadiumName() { return stadiumName; }
    public void setStadiumName(String stadiumName) { this.stadiumName = stadiumName; }

    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

    public String getCoachName() { return coachName; }
    public void setCoachName(String coachName) { this.coachName = coachName; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }
}
