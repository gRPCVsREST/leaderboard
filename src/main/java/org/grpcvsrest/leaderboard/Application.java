package org.grpcvsrest.leaderboard;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.grpcvsrest.leaderboard.grpc.LeaderboardGrpcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application {

    @Autowired
    private LeaderboardGrpcEndpoint grpcEndpoint;

    @PostConstruct
    public void startGrpcServer() throws IOException {
        Server grpcServer = NettyServerBuilder.forPort(8090).addService(grpcEndpoint).build();
        grpcServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(grpcServer::shutdown));
    }

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
