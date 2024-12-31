package com.example.jeon.S3Service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.*;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Service
public class ProfileService {


    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.access-key-id}")
    private String accessKeyId;
    @Value("${aws.region}")
    private String region;
    @Value("${aws.secret-access-key}")
    private String secretAccessKey;
    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    public ProfileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    private final S3AsyncClient s3AsyncClient;



    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> saveFile(@RequestParam String itemName, @RequestParam MultipartFile file) throws IOException {
        //.getParts()로 모든 항목들을 collection형식으로 받을 수 있다
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
        }

        String savePath = "./save/";
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        File saveFile = new File(savePath + uniqueFileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        file.transferTo(saveFile);
        try {
            CompletableFuture<PutObjectResponse> future = s3Actions.uploadLocalFileAsync(bucketName, key, objectPath);
            future.join();
            logger.info("File uploaded successfully to {}/{}", bucketName, key);

        } catch (RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause instanceof S3Exception s3Ex) {
                logger.info("S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode());
            } else {
                logger.info("An unexpected error occurred: " + rt.getMessage());
            }
            throw cause;
        }
        return ResponseEntity.ok(Map.of(
                "message", "File saved successfully",
                "fileName", uniqueFileName,
                "path", saveFile.getAbsolutePath()
        ));
    }
}