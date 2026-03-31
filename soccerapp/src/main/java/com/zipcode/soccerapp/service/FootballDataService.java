package com.zipcode.soccerapp.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class FootballDataService {

    private final RestClient restClient;

    public FootballDataService(
            @Value("${football.api.url}") String apiUrl,
            @Value("${football.api.key}") String apiKey) {

        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("X-Auth-Token", apiKey)
                .build();
    }

    // ── Response Records ──────────────────────────────────────────────────────

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiTeamsResponse(ApiCompetition competition, List<ApiTeam> teams) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiCompetition(String name, ApiArea area) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiArea(String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiTeam(Long id, String name, String venue, Integer founded,
                          ApiCoach coach, List<ApiPlayer> squad) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiCoach(String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ApiPlayer(String name, String position, Integer shirtNumber,
                            String nationality, String dateOfBirth) {}

    // ── API call ──────────────────────────────────────────────────────────────

    public ApiTeamsResponse fetchTeamsByCompetition(int competitionId) {
        return restClient.get()
                .uri("/competitions/{id}/teams", competitionId)
                .retrieve()
                .body(ApiTeamsResponse.class);
    }
}
