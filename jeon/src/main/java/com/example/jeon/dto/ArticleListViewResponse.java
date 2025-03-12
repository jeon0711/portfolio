package com.example.jeon.dto;
import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ArticleListViewResponse {//article들을 글줄로 보는거, 이미지나 content는 안들어

    private final Long id;
    private final String title;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author=article.getAuthor().getEmail();
        this.createdAt=article.getCreatedAt();
        this.updatedAt=article.getUpdatedAt();
    }
}