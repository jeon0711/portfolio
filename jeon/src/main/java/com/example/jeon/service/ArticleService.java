package com.example.jeon.service;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.User;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    public Article save(AddArticleRequest request, String userName) {
        User user=userService.findByEmail(userName);
        return articleRepository.save(request.toEntity(user));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }
    public List<Article> findByAuthor(String userEmail)
    {
        User author=userService.findByEmail(userEmail);
        List<Article> rt=articleRepository.findAllByAuthor_Id(author.getId());
        if(rt.isEmpty())
        {
            return null;
        }
        return rt;
    }

    public void delete(long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        articleRepository.delete(article);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found : " + id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }

    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}
