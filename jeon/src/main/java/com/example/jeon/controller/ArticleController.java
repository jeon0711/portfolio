package com.example.jeon.controller;

import com.example.jeon.ArticleService.ArticleService;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.ArticleResponse;
import com.example.jeon.dto.UpdateArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/articles")
    public  ResponseEntity<List<ArticleResponse>> findAllArticle()
    {
        List<ArticleResponse> articles=articleService.findAll().stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok().body(articles);
    }
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id)
    {
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request)
    {
        Article updatedArticle=articleService.update(id,request);
        return ResponseEntity.ok().body(updatedArticle);
    }
}
