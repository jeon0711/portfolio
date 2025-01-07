package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ResumeResponse {
        private final List<Article> articles;
        private final UserProfile userProfile;
}
