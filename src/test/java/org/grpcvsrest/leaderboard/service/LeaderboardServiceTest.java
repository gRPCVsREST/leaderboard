package org.grpcvsrest.leaderboard.service;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LeaderboardServiceTest {

    private LeaderboardService service = new LeaderboardService();

    @Test
    public void testLeaderboard() {
        service.addVote("Pokemon", "Tarzan", true);
        service.addVote("Pokemon", "Tarzan", true);
        service.addVote("Pokemon", "Tarzan", true);
        service.addVote("Pokemon", "Tarzan", true);

        service.addVote("Pokemon", "Jane", true);
        service.addVote("Pokemon", "Jane", true);
        service.addVote("Pokemon", "Jane", true);

        service.addVote("Pokemon", "John Doe", true);
        service.addVote("Pokemon", "John Doe", true);
        service.addVote("Pokemon", "John Doe", true);
        service.addVote("Pokemon", "John Doe", false);

        service.addVote("Pokemon", "Jane Doe", true);
        service.addVote("Pokemon", "Jane Doe", false);
        service.addVote("Pokemon", "Jane Doe", false);

        service.addVote("Pokemon", "Foo Doe", false);
        service.addVote("Pokemon", "Foo Doe", false);
        service.addVote("Pokemon", "Foo Doe", false);

        service.addVote("Pokemon", "Bar Doe", false);
        service.addVote("Pokemon", "Bar Doe", false);

        service.addVote("Pokemon", "Zero", true);

        assertThat(service.getLeaderboard("Pokemon").getLines())
                .extracting(Leaderboard.Line::getUserId)
                .containsExactly("Tarzan", "Jane", "John Doe", "Jane Doe", "Foo Doe");

    }

}