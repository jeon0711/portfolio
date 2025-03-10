package com.example.jeon.service;

import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProfileService {



    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final ImageService imageService;
    private final UserProfileRepository userProfileRepository;
    @Transactional
    public UserProfile saveProfile(String title,String content, String name,MultipartFile file) throws Throwable {

        try {

          Image srt= imageService.saveImage(name,file);
            UserProfile rt = UserProfile.builder().title(title).content(content).author(name).build();
            srt.setUserProfile(rt);
            rt.addImage(srt);
            userProfileRepository.save(rt);

            return rt;
        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause instanceof S3Exception s3Ex) {
                logger.info("S3 error occurred: Error message: {}, Error code {}", s3Ex.getMessage(), s3Ex.awsErrorDetails().errorCode());
            } else {
                logger.info("An unexpected error occurred: " + rt.getMessage());
            }

            throw cause;
        }

    }

    public UserProfile searchProfile(String name) throws Throwable {

        try {
            return userProfileRepository.findByAuthor(name).orElse(null);

        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();

                logger.info("An unexpected error occurred: " + rt.getMessage());

            throw cause;
        }

    }
}
