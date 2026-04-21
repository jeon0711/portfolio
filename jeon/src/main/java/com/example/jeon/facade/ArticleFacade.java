package com.example.jeon.facade;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.service.ArticleImageService;
import com.example.jeon.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleFacade {
    private final ArticleService articleService;
    private final ArticleImageService articleImageService;

    @Transactional
    public Article saveArticleWithImages(AddArticleRequest request, String userName, List<MultipartFile> images) {
        // 1. 게시글 기본 정보 저장
        Article article = articleService.saveOnlyArticle(request, userName);

        // 2. 이미지 처리 (이미지 서비스에 위임)
        if (images != null && !images.isEmpty()) {
                articleImageService.saveImages(article,images);
            }
        return article;
    }
    @Transactional
    public Article updateArticleWithImages(Long id,UpdateArticleRequest request, List<MultipartFile> images) {
        // 1. 게시글 기본 정보 저장
        Article article = articleService.updateOnlyArticle(id, request);
        if (request.getDeleteImageIds() != null) {
            for (Long imageId : request.getDeleteImageIds()) {
                articleImageService.softDeleteImage(imageId); // DB 및 S3 처리 위임
                // article 내부 리스트에서도 제거 (도메인 로직)
                article.getImages().removeIf(img -> img.getId().equals(imageId));
            }
        }
        // 2. 이미지 처리 (이미지 서비스에 위임)
        if (images != null && !images.isEmpty()) {
            articleImageService.saveImages(article,images);

        }
        return article;
    }
    public List<Article> findAll() {
        return articleService.findAll();
    }

    public Article findById(long id) {
        return articleService.findById(id);
    }
    public List<Article> findByAuthor(String userEmail)
    {
        return articleService.findByAuthor(userEmail);
    }

    public void delete(long id) {
       articleService.delete(id);
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().getEmail().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }
}