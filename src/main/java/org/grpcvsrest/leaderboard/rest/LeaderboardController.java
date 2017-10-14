package org.grpcvsrest.leaderboard.rest;

import org.grpcvsrest.leaderboard.service.Leaderboard;
import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard/{category}")
    public Leaderboard leaderboard(@PathVariable("category") String category) {
        return new Leaderboard(
                5,
                Arrays.asList(
                        new Leaderboard.Line("Tarzan", 1, 3),
                        new Leaderboard.Line("Jane", 0, 2)));
        //return leaderboardService.getLeaderboard(category);
    }
}
