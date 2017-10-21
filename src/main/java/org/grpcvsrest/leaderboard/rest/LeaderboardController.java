package org.grpcvsrest.leaderboard.rest;

import org.grpcvsrest.leaderboard.service.Leaderboard;
import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @PutMapping("/leaderboard/vote/{category}/{user_id}/{guessed}")
    public void acceptVote(
            @PathVariable("category") String category,
            @PathVariable("user_id") String userId,
            @PathVariable("guessed") boolean guessed) {
        leaderboardService.addVote(category, userId, guessed);
    }

    @GetMapping("/leaderboard/{category}")
    public Leaderboard leaderboard(@PathVariable("category") String category) {
//        return new Leaderboard(
//                5,
//                Arrays.asList(
//                        new Leaderboard.Line("Tarzan", 1, 3),
//                        new Leaderboard.Line("Jane", 0, 2)));
        return leaderboardService.getLeaderboard(category);
    }
}
