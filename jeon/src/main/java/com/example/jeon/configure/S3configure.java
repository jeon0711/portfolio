package com.example.jeon.configure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.retry.RetryMode;

import java.time.Duration;

@Configuration
public class S3configure {

    @Bean
    public S3AsyncClient s3configure() {
        // Netty HTTP Client 설정
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .maxConcurrency(50)  // 동시 연결 수 제한
                .connectionTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();

        // Client Override 설정
        ClientOverrideConfiguration overrideConfig = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(2))
                .apiCallAttemptTimeout(Duration.ofSeconds(90))
                .retryPolicy(RetryMode.STANDARD)
                .build();

        // S3AsyncClient 생성 및 반환
        return S3AsyncClient.builder()
                .region(Region.US_EAST_1) // 원하는 리전을 설정
                .httpClient(httpClient)
                .overrideConfiguration(overrideConfig)
                .build();
    }
}