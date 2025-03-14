package com.example.jeon.service;

import com.example.jeon.domain.Image;
import com.example.jeon.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ImageService {


    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.region}")
    private String region;
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private final S3AsyncClient s3AsyncClient;
    private final ImageRepository imageRepository;
    public CompletableFuture<PutObjectResponse> uploadLocalFileAsync(String bucketName, String key, String objectPath) {
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CompletableFuture<PutObjectResponse> response = s3AsyncClient.putObject(objectRequest, AsyncRequestBody.fromFile(Paths.get(objectPath)));
        return response.whenComplete((resp, ex) -> {
            if (ex != null) {
                throw new RuntimeException("Failed to upload file", ex);
            }
        });
    }
    public CompletableFuture<DeleteObjectResponse> deleteObjectAsync(String bucketName, String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        CompletableFuture<DeleteObjectResponse> response = s3AsyncClient.deleteObject(deleteRequest);

        return response.whenComplete((resp, ex) -> {
            if (ex != null) {
                throw new RuntimeException("🚨 Failed to delete file from S3: " + key, ex);
            } else {
                logger.info("✅ Successfully deleted file from S3: {}", key);
            }
        });
    }
    @Transactional
    public Image saveImage(String name, MultipartFile file) throws Throwable //저장된 image의 url을 리턴
    {
        //.getParts()로 모든 항목들을 collection형식으로 받을 수 있다
        String savePath = System.getProperty("user.dir") + "/profile_image/"; // 절대 경로 사용
        File saveDir = new File(savePath);

        // ✅ 폴더가 없으면 생성
        if (!saveDir.exists()) {
            boolean isCreated = saveDir.mkdirs();
            if (!isCreated) {
                throw new IOException("🚨 폴더 생성 실패: " + savePath);
            }
        }

        // ✅ 파일 존재 여부 확인
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("🚨 파일이 비어 있거나 null입니다.");
        }

        // ✅ 고유한 파일명 생성
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new IllegalArgumentException("🚨 유효하지 않은 파일명: " + originalFileName);
        }

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = name + "_" + UUID.randomUUID().toString() + fileExtension;
        File saveFile = new File(savePath + uniqueFileName);
        logger.info("local image 저장 시도: " + saveFile.getAbsolutePath());
        String s3Key = "profile_image/" + uniqueFileName;
        try {
            file.transferTo(saveFile);
            logger.info("✅ local image 저장 성공: " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("🚨 파일 저장 중 오류 발생: " + e.getMessage(), e);
            throw new RuntimeException("파일 저장 실패", e);
        }
        logger.info("local image저장성공");
        try {
            CompletableFuture<PutObjectResponse> future = uploadLocalFileAsync(bucketName,s3Key, savePath + uniqueFileName);
            future.join();
        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause instanceof S3Exception s3Ex) {
                logger.info("S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode());
            } else {
                logger.info("An unexpected error occurred: " + rt.getMessage());
            }
            saveFile.delete();
            throw cause;
        }
        saveFile.delete();

        String url=String.format("https://%s.s3.%s.amazonaws.com/%s",
                bucketName,
                region, // 자신의 리전에 맞게 설정
                s3Key
        );
        Image rt=new Image();
        rt.setUrl(url);
        rt.setActive(true);
        return imageRepository.save(rt);

    }
    @Transactional
    public void deleteImage(long id,String filename) throws Throwable //저장된 image의 url을 리턴
    {

        try {
            CompletableFuture<DeleteObjectResponse> future = deleteObjectAsync(bucketName,filename);
            future.join();
        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause instanceof S3Exception s3Ex) {
                logger.info("S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode());
            } else {
                logger.info("An unexpected error occurred: " + rt.getMessage());
            }
            throw cause;
        }
      Image d=imageRepository.getReferenceById(id);
        imageRepository.delete(d);
    }
    @Transactional
    public void softDeleteImage(long id) throws Throwable //저장된 image의 url을 리턴
    {

        try{
            Image d=imageRepository.getReferenceById(id);
            d.setActive(false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
