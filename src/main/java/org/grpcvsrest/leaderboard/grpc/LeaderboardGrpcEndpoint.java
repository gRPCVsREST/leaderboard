package org.grpcvsrest.leaderboard.grpc;

import com.grpcvsrest.grpc.LeaderboardServiceGrpc.LeaderboardServiceImplBase;
import com.grpcvsrest.grpc.RecordVoteRequest;
import com.grpcvsrest.grpc.RecordVoteResponse;
import io.grpc.stub.StreamObserver;
import org.grpcvsrest.leaderboard.service.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Leaderboard gRPC endpoint.
 */
@Service
public class LeaderboardGrpcEndpoint extends LeaderboardServiceImplBase {

    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardGrpcEndpoint(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @Override
    public void recordVote(RecordVoteRequest request, StreamObserver<RecordVoteResponse> responseObserver) {
        String votedCategory = request.getVotedCategory().name();

        leaderboardService.addVote(votedCategory, request.getUsername(), request.getRightGuess());

        responseObserver.onNext(RecordVoteResponse.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
