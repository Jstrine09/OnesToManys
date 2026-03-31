package com.zipcode.soccerapp.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "leagues")
public class League {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private Integer foundedYear;
    private String commissioner;

    @OneToMany(mappedBy = "league", cascade = CascadeType.ALL)
    private List<Club> clubs;

    public League() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getFoundedYear() { return foundedYear; }
    public void setFoundedYear(Integer foundedYear) { this.foundedYear = foundedYear; }

    public String getCommissioner() { return commissioner; }
    public void setCommissioner(String commissioner) { this.commissioner = commissioner; }

    public List<Club> getClubs() { return clubs; }
    public void setClubs(List<Club> clubs) { this.clubs = clubs; }
}
