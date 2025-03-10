package com.example.jeon.service;

import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.repository.ImageRepository;
import com.example.jeon.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
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
    @Transactional
    public Image saveImage(String name, MultipartFile file) throws Throwable //저장된 image의 url을 리턴
    {
        //.getParts()로 모든 항목들을 collection형식으로 받을 수 있다
        String savePath = "./profile_image/";
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uniqueFileName = name+ UUID.randomUUID().toString() + fileExtension;

        File saveFile = new File(savePath + uniqueFileName);
        if (!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        file.transferTo(saveFile);
        logger.info("local image저장성공");
        try {
            CompletableFuture<PutObjectResponse> future = uploadLocalFileAsync(bucketName,savePath+uniqueFileName, savePath + uniqueFileName);
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
                savePath+uniqueFileName
        );
        Image rt=new Image();
        rt.setUrl(url);
        return imageRepository.save(rt);

    }

}
