package com.example.jeon.controller;

import com.example.jeon.ArticleService.ArticleService;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request)
    {
        Article saveArticle=articleService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
    }
}
