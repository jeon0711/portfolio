package com.example.jeon.ArticleService;

import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.repository.ArticleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Service
@RequestMapping("/article")
public class ArticleService {
    private final ArticleRepository articleRepository;
    public Article save(AddArticleRequest request)
    {
        return articleRepository.save(request.toEntity());
    }
    public List<Article> findAll()
    {
        return articleRepository.findAll();
    }
    public void delete(long id)
    {
        articleRepository.deleteById(id);
    }
    @Transactional
    public Article update(long id, UpdateArticleRequest request)
    {
        Article article=articleRepository.findById(id).orElseThrow(()->new IllegalArgumentException("not found"+id));
        article.update(request.getTitle(),request.getContent());
        return article;
    }
}
