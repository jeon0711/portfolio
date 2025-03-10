package com.example.jeon.controller;

import com.example.jeon.service.ProfileService;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserProfile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.ArticleResponse;
import com.example.jeon.dto.UpdateArticleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/")
    public ResponseEntity<UserProfile> saveProfile(HttpServletRequest request, @ModelAttribute AddUserProfile input ) throws IOException {
        try {
            String referer = request.getHeader("Referer"); // 요청을 보낸 페이지의 URL
            String requestURL = request.getRequestURL().toString(); //
            UserProfile rt = profileService.saveProfile(input.getTitle(), input.getContent(),input.getAuthor(), input.getImage());
            return ResponseEntity.created(URI.create(requestURL)).body(rt);
        }
        catch(Throwable e)
        {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/{id}")
    public  ResponseEntity<List<ArticleResponse>> findAllArticle()//profile포함 전체
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
