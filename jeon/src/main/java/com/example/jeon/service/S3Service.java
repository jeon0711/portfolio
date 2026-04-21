package com.example.jeon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import com.example.jeon.domain.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3AsyncClient s3AsyncClient;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    /**
     * 파일을 S3에 업로드하고 접근 가능한 URL을 반환합니다.
     * @param file 업로드할 멀티파트 파일
     * @param folderPath S3 내 저장 폴더 경로 (예: "articles/" 또는 "profiles/")
     * @return 업로드된 파일의 S3 URL
     */
    public String uploadFile(MultipartFile file, String folderPath) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있습니다.");
        }

        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String s3Key = folderPath + fileName;

        // 1. 임시 파일 생성
        File tempFile = createTempFile(file);

        try {
            // 2. S3 업로드 실행
            putObjectToS3(s3Key, tempFile);

            // 3. 성공 시 URL 반환
            return generateS3Url(s3Key);
        } finally {
            // 4. 어떤 경우에도 임시 파일은 반드시 삭제 (리소스 관리)
            deleteTempFile(tempFile);
        }
    }

    /**
     * S3에서 파일을 삭제합니다.
     * @param s3Key 삭제할 파일의 키
     */
    public void deleteFile(String s3Key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        s3AsyncClient.deleteObject(deleteRequest)
                .whenComplete((resp, ex) -> {
                    if (ex != null) {
                        log.error("🚨 S3 파일 삭제 실패: {}", s3Key, ex);
                    } else {
                        log.info("✅ S3 파일 삭제 성공: {}", s3Key);
                    }
                });
    }

    private void putObjectToS3(String s3Key, File file) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build();

        // 트랜잭션 밖에서 호출된다는 가정하에 join()으로 완료 보장
        s3AsyncClient.putObject(putRequest, AsyncRequestBody.fromFile(file)).join();
    }

    private File createTempFile(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("s3_upload_", "_" + file.getOriginalFilename());
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new RuntimeException("임시 파일 생성 중 오류 발생", e);
        }
    }

    private void deleteTempFile(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                log.warn("⚠️ 임시 파일 삭제 실패: {}", file.getAbsolutePath());
            }
        }
    }

    private String generateUniqueFileName(String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    private String generateS3Url(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
    }
    /**
     * S3에서 파일을 물리적으로 삭제합니다.
     * @param url 삭제할 파일의 전체 URL
     */
    public void deleteFileByUrl(String url) {
        if (url == null || url.isEmpty()) return;

        try {
            // 1. URL에서 S3 Key 추출 (예: https://.../articles/uuid_name.jpg -> articles/uuid_name.jpg)
            String key = extractKeyFromUrl(url);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // 2. 비동기 삭제 실행 (삭제는 결과 대기가 필수가 아닌 경우가 많으므로 join 생략 가능)
            s3AsyncClient.deleteObject(deleteRequest)
                    .whenComplete((resp, ex) -> {
                        if (ex != null) {
                            log.error("🚨 S3 물리 파일 삭제 실패 (URL: {}): {}", url, ex.getMessage());
                        } else {
                            log.info("✅ S3 물리 파일 삭제 완료 (Key: {})", key);
                        }
                    });
        } catch (Exception e) {
            log.error("🚨 S3 Key 추출 중 오류 발생: {}", e.getMessage());
        }
    }

    private String extractKeyFromUrl(String url) {
        // S3 URL 구조: https://{bucket}.s3.{region}.amazonaws.com/{key}
        // .com/ 이후의 문자열이 Key입니다.
        String splitStr = ".amazonaws.com/";
        return url.substring(url.lastIndexOf(splitStr) + splitStr.length());
    }
}