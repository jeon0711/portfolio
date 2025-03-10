package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SynthesisResponse {
        private List<Article> articles;
        private UserProfile userProfile;
}
