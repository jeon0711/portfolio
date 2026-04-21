package com.example.jeon.service;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.ArticleImage;
import com.example.jeon.repository.ArticleImageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleImageService {
    private final S3Service s3Service; // 인프라 서비스 주입
    private final ArticleImageRepository articleImageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ArticleImageService.class);
    @Transactional
    public void saveImages(Article article, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            // 1. 물리적 업로드 (S3Service에 위임)
            String url = s3Service.uploadFile(file, "articles/");

            // 2. 비즈니스 엔티티 생성 및 저장
            ArticleImage img = ArticleImage.builder()
                    .url(url)
                    .article(article)
                    .isActive(true)
                    .build();
            article.addImage(img);
            articleImageRepository.save(img);
        }
    }
    /**
     * 이미지 완전 삭제 (DB + S3)
     */
    @Transactional
    public void deleteImage(Long imageId) {
        ArticleImage image = articleImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이미지가 존재하지 않습니다."));

        // 1. S3 물리 파일 삭제 요청
        s3Service.deleteFileByUrl(image.getUrl());

        // 2. DB 레코드 삭제
        articleImageRepository.delete(image);
        logger.info("✅ 이미지 레코드 삭제 완료 (ID: {})", imageId);
    }

    /**
     * 이미지 소프트 삭제 (상태만 변경)
     */
    @Transactional
    public void softDeleteImage(Long imageId) {
        ArticleImage image = articleImageRepository.findById(imageId)
                .orElseThrow(() -> new EntityNotFoundException("해당 이미지가 존재하지 않습니다."));

        image.setActive(false);
        // 소프트 삭제 시에는 보통 S3 파일을 바로 지우지 않습니다.
        // 나중에 배치 작업으로 일괄 정리하거나 보관용으로 남겨둡니다.
    }
}
