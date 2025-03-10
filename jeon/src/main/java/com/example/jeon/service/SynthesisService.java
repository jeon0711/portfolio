package com.example.jeon.service;

import com.example.jeon.domain.User;
import com.example.jeon.dto.SynthesisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            rt.setArticles(articleService.findByAuthor(id));
            return Optional.of(rt);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}