package com.example.jeon.controller;

import com.example.jeon.S3Service.ProfileService;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserProfile;
import org.apache.catalina.User;
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
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.profiles.Profile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/")
    public ResponseEntity<UserProfile> saveFile(@RequestParam String itemName, @RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            UserProfile rt = profileService.saveProfile(itemName, file);
            return ResponseEntity.ok().body(rt);
        }
        catch(Throwable e)
        {
            return ResponseEntity.internalServerError().build();
        }

    }
    @GetMapping("/{id}")
    public  ResponseEntity<List<ArticleResponse>> findAllArticle()
    {
        return ResponseEntity.ok().build();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id)
    {

        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request)
    {

        return ResponseEntity.ok().build();
    }
}
