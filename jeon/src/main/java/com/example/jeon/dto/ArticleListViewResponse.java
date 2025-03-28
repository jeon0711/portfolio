package com.example.jeon.dto;
import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ArticleListViewResponse {//article들을 글줄로 보는거, 이미지나 content는 안들어

    private  Long id;
    private  String title;
    private String author;
    private String preview;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> skills;
    public ArticleListViewResponse(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author=article.getAuthor().getEmail();
        this.createdAt=article.getCreatedAt();
        this.updatedAt=article.getUpdatedAt();
        this.preview= Arrays.stream(article.getContent().split("\n"))
                .limit(3)
                .collect(Collectors.joining("\n"));
        this.skills=article.getSkills();
    }
}