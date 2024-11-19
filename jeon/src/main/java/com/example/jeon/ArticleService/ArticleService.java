package com.example.jeon.ArticleService;

import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    public Article save(AddArticleRequest request)
    {
        return articleRepository.save(request.toEntity());
    }
}
