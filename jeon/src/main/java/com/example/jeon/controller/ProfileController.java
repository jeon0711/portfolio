package com.example.jeon.controller;

import com.example.jeon.S3Service.ProfileService;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserProfile;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.apache.tomcat.util.http.fileupload.FileUpload;
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
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/")
    public ResponseEntity<UserProfile> saveFile(HttpServletRequest request, @RequestPart("image") MultipartFile image, // 파일 부분
                                                @RequestPart("data") AddUserProfile input ) throws IOException {
        try {
            String referer = request.getHeader("Referer"); // 요청을 보낸 페이지의 URL
            String requestURL = request.getRequestURL().toString(); //
            UserProfile rt = profileService.saveProfile(input.getTitle(), input.getContent(),input.getName(), image);
            return ResponseEntity.created(URI.create(requestURL)).body(rt);
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
