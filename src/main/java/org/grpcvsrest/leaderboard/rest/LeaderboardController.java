package org.grpcvsrest.leaderboard.rest;

import org.grpcvsrest.leaderboard.service.Leaderboard;
import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard/{category}")
    public Leaderboard leaderboard(@PathVariable("category") String category) {
        return leaderboardService.getLeaderboard(category);
    }
}
