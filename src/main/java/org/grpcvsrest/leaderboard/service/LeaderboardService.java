package org.grpcvsrest.leaderboard.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private final static Logger LOG = LoggerFactory.getLogger(LeaderboardService.class);

    private final ConcurrentMap<String, ConcurrentMap<String, UserVotes>> votesByCategory = new ConcurrentHashMap<>();
    private final AtomicInteger totalVotes = new AtomicInteger(0);

    public void addVote(String category, String userId, boolean rightGuess) {
        ConcurrentMap<String, UserVotes> newMap = new ConcurrentHashMap<>();
        ConcurrentMap<String, UserVotes> existingMap = votesByCategory.putIfAbsent(category, newMap);
        ConcurrentMap<String, UserVotes> userVoteMap = existingMap != null ? existingMap : newMap;

        UserVotes votes = userVoteMap.putIfAbsent(userId, new UserVotes(1, rightGuess ? 1 : 0));
        if (votes != null) {
            votes.total.incrementAndGet();
            if (rightGuess) {
                votes.correct.incrementAndGet();
            }
        }

        totalVotes.incrementAndGet();
    }

    public Leaderboard getLeaderboard(String category) {
        Map<String, UserVotes> currentVotes = ImmutableMap.copyOf(votesByCategory.get(category) == null ? new HashMap<>() : votesByCategory.get(category));

        List<Leaderboard.Line> rawLines = currentVotes.entrySet().stream()
                .map(e -> new Leaderboard.Line(
                        e.getKey(),
                        (double) e.getValue().correct.get() / (double) e.getValue().total.get(),
                        e.getValue().total.get())
                )
                .collect(Collectors.toList());

        Collections.sort(rawLines, Comparator.comparingInt(l -> -l.getTotalVotes()));
        if (rawLines.size() > 6) {
            rawLines = rawLines.stream().limit((long) (rawLines.size()*0.8)).collect(Collectors.toList());
        }

        LOG.info("raw lines, without bottom 20% voters, {}", rawLines);
        Collections.sort(rawLines, Comparator.comparingDouble(l -> -l.getScore()));
        LOG.info("top score, {}", rawLines);

        return new Leaderboard(totalVotes.get(), rawLines);

    }

    private static class UserVotes {
        private final AtomicInteger total;
        private final AtomicInteger correct;

        private UserVotes(int total, int correct) {
            this.total = new AtomicInteger(total);
            this.correct = new AtomicInteger(correct);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserVotes userVotes = (UserVotes) o;
            return Objects.equal(total, userVotes.total) &&
                    Objects.equal(correct, userVotes.correct);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(total, correct);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("total", total)
                    .add("correct", correct)
                    .toString();
        }
    }

}
