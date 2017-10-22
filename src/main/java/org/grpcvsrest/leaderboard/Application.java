package org.grpcvsrest.leaderboard;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import org.grpcvsrest.leaderboard.grpc.LeaderboardGrpcEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.SpanAdjuster;
import org.springframework.context.annotation.Bean;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.urlconnection.URLConnectionSender;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static brave.sampler.Sampler.ALWAYS_SAMPLE;

@SpringBootApplication
public class Application {

    @Autowired
    private LeaderboardGrpcEndpoint grpcEndpoint;
    @Autowired
    private GrpcTracing grpcTracing;

    @Bean
    public SpanAdjuster customSpanAdjuster(@Value("${spring.application.name}") String appName) {
        return span -> span.toBuilder().name("#" + appName + "/" + span.getName().replace("http:/", "")).build();
    }

    @PostConstruct
    public void startGrpcServer() throws IOException {
        Server grpcServer = NettyServerBuilder.forPort(8090).addService(grpcEndpoint)
                .intercept(grpcTracing.newServerInterceptor())
                .build();
        grpcServer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(grpcServer::shutdown));
    }

    @Bean
    public GrpcTracing grpcTracing(@Value("${zipkin_service_host:zipkin}") String zipkinHost,
                                   @Value("${zipkin_service_port:9411}") int zipkinPort) {

        URLConnectionSender sender = URLConnectionSender.newBuilder()
                .endpoint(String.format("http://%s:%s/api/v2/spans", zipkinHost, zipkinPort))
                .build();

        return GrpcTracing.create(Tracing.newBuilder()
                .sampler(ALWAYS_SAMPLE)
                .spanReporter(AsyncReporter.create(sender))
                .build());
    }

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}
