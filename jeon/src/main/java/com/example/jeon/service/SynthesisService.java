package com.example.jeon.service;

import com.example.jeon.domain.User;
import com.example.jeon.dto.SynthesisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            rt.setArticles(
                    articleService.findByAuthor(id).stream()
                            .map(ArticleListViewResponse::new)
                            .collect(Collectors.toList())
            );

            return Optional.of(rt);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}