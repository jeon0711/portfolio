package com.example.jeon.service;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.User;
import com.example.jeon.dto.SynthesisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.jeon.dto.ArticleListViewResponse;

@RequiredArgsConstructor
@Service
public class SynthesisService {
    private final UserService userService;
    private final ProfileService profileService;
    private final ArticleService articleService;

    public Optional<SynthesisResponse> findByEmail(String id) {
        try {
            SynthesisResponse rt = new SynthesisResponse();
            rt.setUserProfile(profileService.searchProfile(id));

            // 수정된 부분: null 체크 후 빈 리스트 또는 스트림 처리
            List<Article> articles = articleService.findByAuthor(id);

            if (articles != null) {
                rt.setArticles(
                        articles.stream()
                                .map(ArticleListViewResponse::new)
                                .collect(Collectors.toList())
                );
            } else {
                rt.setArticles(Collections.emptyList()); // 데이터 없으면 빈 리스트 세팅
            }

            return Optional.of(rt);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}