package com.example.jeon.controller;

import com.example.jeon.service.ArticleService;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.ArticleResponse;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles" )
public class ArticleController {
    private final ArticleService articleService;
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @PostMapping(value="/")
    public ResponseEntity<Article> addArticle(@ModelAttribute AddArticleRequest request,
                                              @RequestParam(value = "images" , required = false) List<MultipartFile> images, Principal principal) {
        Article savedArticle = articleService.save(request, principal.getName(),images);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedArticle);
    }

    @GetMapping("/")
    public  ResponseEntity<List<ArticleResponse>> findAllArticle()
    {
        List<ArticleResponse> articles=articleService.findAll().stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok().body(articles);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id)
    {
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping(value="/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @ModelAttribute UpdateArticleRequest request, @RequestParam(value = "images" , required = false) List<MultipartFile> images)
    {
        Article updatedArticle=articleService.update(id,request,images);
        return ResponseEntity.ok().body(updatedArticle);
    }
}
