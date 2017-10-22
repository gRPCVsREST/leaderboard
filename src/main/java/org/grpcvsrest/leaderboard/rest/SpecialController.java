package org.grpcvsrest.leaderboard.rest;

import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public SpecialController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PostMapping(value = "/clear", headers = "secret=42")
    public void clear() {
        leaderboardService.clear();
    }

    @PostMapping(value = "/break", headers = "secret=42")
    public void breakService() {
        leaderboardService.breakService();
    }

    @PostMapping(value = "/unbreak", headers = "secret=42")
    public void unbreak() {
        leaderboardService.unbreak();
    }


}
