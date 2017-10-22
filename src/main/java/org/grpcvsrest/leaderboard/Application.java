package org.grpcvsrest.leaderboard;

import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.grpcvsrest.leaderboard.grpc.LeaderboardGrpcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.SpanAdjuster;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication
public class Application {

    @Autowired
    private LeaderboardGrpcEndpoint grpcEndpoint;

    @Bean
    public SpanAdjuster customSpanAdjuster(@Value("${spring.application.name}") String appName) {
        return span -> span.toBuilder().name("#" + appName + "/" + span.getName().replace("http:/", "")).build();
    }

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
