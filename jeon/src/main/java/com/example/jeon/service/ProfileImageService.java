package com.example.jeon.service;

import com.example.jeon.domain.*;
import com.example.jeon.repository.ProfileImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProfileImageService {
    private final S3Service s3Service; // 인프라 서비스 주입
    private final ProfileImageRepository profileImageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProfileImageService.class);
    @Transactional
    public void saveImage(UserProfile user,MultipartFile file) {

            String url = s3Service.uploadFile(file, "profile/");

            // 2. 비즈니스 엔티티 생성 및 저장
            ProfileImage img = ProfileImage.builder()
                    .url(url)
                    .user(user)
                    .isActive(true)
                    .build();

            profileImageRepository.save(img);

    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProfileImage image = profileImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이미지가 존재하지 않습니다."));

        // 1. S3 물리 파일 삭제 요청
        s3Service.deleteFileByUrl(image.getUrl());

        // 2. DB 레코드 삭제
        profileImageRepository.delete(image);
        logger.info("✅ 이미지 레코드 삭제 완료 (ID: {})", imageId);
    }

    /**
     * 이미지 소프트 삭제 (상태만 변경)
     */
    @Transactional
    public void softDeleteImage(Long imageId) {
        ProfileImage image = profileImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이미지가 존재하지 않습니다."));

        image.setActive(false);
        // 소프트 삭제 시에는 보통 S3 파일을 바로 지우지 않습니다.
        // 나중에 배치 작업으로 일괄 정리하거나 보관용으로 남겨둡니다.
    }
}
