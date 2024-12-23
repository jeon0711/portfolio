package com.example.jeon.controller;

import com.example.jeon.S3Service.ProfileService;
import com.example.jeon.dto.AddUserProfile;
import org.springframework.web.bind.annotation.RequestMapping;
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
import software.amazon.awssdk.profiles.Profile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/")
    public ResponseEntity<Profile> addProfile(@RequestBody AddUserProfile request)
    {

        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
    }
    @GetMapping("/{id}")
    public  ResponseEntity<List<ArticleResponse>> findAllArticle()
    {

        return ResponseEntity.ok().body(articles);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id)
    {
        articleService.delete(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request)
    {
        Article updatedArticle=articleService.update(id,request);
        return ResponseEntity.ok().body(updatedArticle);
    }
}
