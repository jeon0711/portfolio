package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import com.example.jeon.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddArticleRequest {
    private String title;
    private String content;
    private List<String> skills;
    public Article toEntity(User author) {
        return Article.builder().title(title).content(content).user(author).skills(skills).build();
    }
}


