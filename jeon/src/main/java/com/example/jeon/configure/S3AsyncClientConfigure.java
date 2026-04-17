package com.example.jeon.configure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.time.Duration;
/*

@Configuration
public class S3AsyncClientConfigure {

    @Bean
    public S3AsyncClient s3AsyncClient() {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .maxConcurrency(50)  // 동시 연결 수 제한
                .connectionTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();
        ClientOverrideConfiguration overrideConfig = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(2))
                .apiCallAttemptTimeout(Duration.ofSeconds(90))
                .retryPolicy(RetryMode.STANDARD)
                .build();
        return software.amazon.awssdk.services.s3.S3AsyncClient.builder()
                .region(Region.AP_NORTHEAST_2) // 원하는 리전을 설정
                .httpClient(httpClient)
                .overrideConfiguration(overrideConfig)
                .build();
    }
}*/

@Configuration
public class S3AsyncClientConfigure {

    // .env.properties에서 읽어올 변수들
    @Value("${AWS_ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS_SECRET_KEY}")
    private String secretKey;

    @Bean
    public S3AsyncClient s3AsyncClient() {
        // 1. 인증 정보 생성
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .maxConcurrency(50)
                .connectionTimeout(Duration.ofSeconds(60))
                .readTimeout(Duration.ofSeconds(60))
                .writeTimeout(Duration.ofSeconds(60))
                .build();

        ClientOverrideConfiguration overrideConfig = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(2))
                .apiCallAttemptTimeout(Duration.ofSeconds(90))
                .retryPolicy(RetryMode.STANDARD)
                .build();

        return S3AsyncClient.builder()
                .region(Region.AP_NORTHEAST_2)
                // 2. 반드시 credentialsProvider를 설정해야 합니다!
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .httpClient(httpClient)
                .overrideConfiguration(overrideConfig)
                .build();
    }
}