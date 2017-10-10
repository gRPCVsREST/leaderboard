package org.grpcvsrest.leaderboard.service;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;

public class Leaderboard {
    private final int total;
    private final List<Line> lines;

    public Leaderboard(int total, List<Line> lines) {
        this.total = total;
        this.lines = lines;
    }

    public int getTotal() {
        return total;
    }

    public List<Line> getLines() {
        return lines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leaderboard that = (Leaderboard) o;
        return total == that.total &&
                Objects.equal(lines, that.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(total, lines);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("total", total)
                .add("lines", lines)
                .toString();
    }

    public static class Line {
        private final String userId;
        private final double score;
        private final int totalVotes;

        public Line(String userId, double score, int totalVotes) {
            this.userId = userId;
            this.score = score;
            this.totalVotes = totalVotes;
        }

        public String getUserId() {
            return userId;
        }

        public double getScore() {
            return score;
        }

        public int getTotalVotes() {
            return totalVotes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Line line = (Line) o;
            return Double.compare(line.score, score) == 0 &&
                    totalVotes == line.totalVotes &&
                    Objects.equal(userId, line.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(userId, score, totalVotes);
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("userId", userId)
                    .add("score", score)
                    .add("totalVotes", totalVotes)
                    .toString();
        }
    }
}
