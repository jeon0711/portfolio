package com.example.jeon.configure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import java.net.URI;
@Configuration
public class S3configure {

        @Bean
        public S3Client s3Client() {
            return S3Client.builder()
                    .region(Region.AP_SOUTHEAST_2)
                    .endpointOverride(URI.create("https://s3.ap-southeast-2.amazonaws.com"))
                    .forcePathStyle(true)
                    .build();

        }
}