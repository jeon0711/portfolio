package com.example.jeon.service;

import com.example.jeon.domain.Image;
import com.example.jeon.domain.User;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.repository.UserProfileRepository;
import com.example.jeon.repository.UserRepository;
import com.example.jeon.util.StringParser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
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
    public UserProfile saveProfile(String title, String content, String name, MultipartFile file, List<String> skills) throws Throwable {

        try {
            UserProfile rt = userProfileRepository.findByAuthor(name).orElseThrow(() -> {
                logger.error("UserProfile not found for author: " + name);
                return new IllegalArgumentException("Unexpected user: " + name);
            });
            logger.info(content);
            logger.info(rt.getAuthor());

            if (title != null && !title.isEmpty()) {

                rt.setTitle(title);
            }

            if (content != null && !content.isEmpty()) {

                rt.setContent(content);
            }
            rt.setSkills(skills);
            if(file!=null && !file.isEmpty()) {
                logger.info("what");
                Image srt = imageService.saveImage(name, file);
                srt.setUserProfile(rt);
                rt.addImage(srt);

            }
            logger.info(rt.getTitle());
            userProfileRepository.save(rt);
            return rt;
        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();
            if (cause == null) {
                logger.error("Unexpected runtime exception without a cause: " + rt.getMessage());
                throw rt; // 원래 예외를 던짐
            }
            else if (cause instanceof S3Exception s3Ex) {
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
