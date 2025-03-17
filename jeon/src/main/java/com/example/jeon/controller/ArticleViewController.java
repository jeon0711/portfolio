package com.example.jeon.controller;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.ArticleListViewResponse;
import com.example.jeon.dto.ArticleViewResponse;
import com.example.jeon.service.ArticleService;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleViewController {

        private final ArticleService articleService;
        @GetMapping("/")
        public String getArticles(Model model, Principal principal) {
            List<ArticleListViewResponse> articles =articleService.findByAuthor(principal.getName())
                    .stream()
                    .map(ArticleListViewResponse::new)
                    .toList();
            model.addAttribute("articles", articles);

            return "redirect:/synthesis/"+principal.getName();
        }

        @GetMapping("/{id}")
        public String getArticle(@PathVariable Long id, Model model) {
            Article article = articleService.findById(id);
            Hibernate.initialize(article.getImages());
            model.addAttribute("article", new ArticleViewResponse(article));

            return "article";
        }


        @GetMapping("/new-article")
        public String newArticle(@RequestParam(required = false) Long id, Model model) {
            if (id == null) {
                model.addAttribute("article", new ArticleViewResponse());
            } else {
                Article article = articleService.findById(id);
                Hibernate.initialize(article.getImages());
                model.addAttribute("article", new ArticleViewResponse(article));
            }

            return "newArticle";
        }
}
