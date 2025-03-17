package com.example.jeon.service;

import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserProfile;
import com.example.jeon.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.model.S3Exception;

@RequiredArgsConstructor
@Service
public class ProfileService {



    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
    private final ImageService imageService;
    private final UserProfileRepository userProfileRepository;
    @Transactional
    public UserProfile saveProfile(AddUserProfile input) throws Throwable {

        try {
            String title=input.getTitle();
            String author=input.getAuthor();
            String content=input.getContent();
            String phone=input.getPhone();
            String name=input.getName();
            MultipartFile file=input.getImage();
            List<String> skills=input.getSkills();
            List<String> externalUrls=input.getExternalUrls();
            UserProfile rt = userProfileRepository.findByAuthor(author).orElseThrow(() -> {
                logger.error("UserProfile not found for author: " + author);
                return new IllegalArgumentException("Unexpected user: " + author);
            });
            if (title != null && !title.isEmpty()) {

                rt.setTitle(title);
            }

            if (content != null && !content.isEmpty()) {

                rt.setContent(content);
            }
            if(name!=null && !name.isEmpty())
            {
                rt.setName(name);
            }
            if(phone!=null && !phone.isEmpty())
            {
                rt.setPhone(phone);
            }
            rt.setSkills(skills);
            rt.setExternalUrls(externalUrls);
            if(file!=null && !file.isEmpty()) {
                Image srt = imageService.saveImage(author, file);
                if (rt.getImage() != null) {

                   // imageService.deleteImage(rt.getImage().getId(),rt.getImage().getUrl());
                    imageService.softDeleteImage(rt.getImage().getId());
                }
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

    public UserProfile searchProfile(String id) throws Throwable {

        try {
            return userProfileRepository.findByAuthor(id).orElse(null);

        }catch(RuntimeException rt) {
            Throwable cause = rt.getCause();

                logger.info("An unexpected error occurred: " + rt.getMessage());

            throw cause;
        }

    }
}
