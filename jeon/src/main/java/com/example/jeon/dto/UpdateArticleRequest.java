package com.example.jeon.dto;

import lombok.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter//setter있어야 spring이 값 주입
public class UpdateArticleRequest {
    private String title;
    private String content;
    private List<String> skills;
}