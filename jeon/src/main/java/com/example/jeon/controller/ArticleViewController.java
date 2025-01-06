package com.example.jeon.controller;
import com.example.jeon.domain.Article;
import com.example.jeon.dto.ArticleListViewResponse;
import com.example.jeon.dto.ArticleViewResponse;
import com.example.jeon.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import java.util.List;
@RequiredArgsConstructor
@Controller
public class ArticleViewController {

        private final ArticleService articleService;

        @GetMapping("/articles")
        public String getArticles(Model model) {
            List<ArticleListViewResponse> articles =articleService.findAll()
                    .stream()
                    .map(ArticleListViewResponse::new)
                    .toList();
            model.addAttribute("articles", articles);

            return "articleList";
        }

        @GetMapping("/articles/{id}")
        public String getArticle(@PathVariable Long id, Model model) {
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));

            return "article";
        }


        @GetMapping("/new-article")
        public String newArticle(@RequestParam(required = false) Long id, Model model) {
            if (id == null) {
                model.addAttribute("article", new ArticleViewResponse());
            } else {
                Article article = articleService.findById(id);
                model.addAttribute("article", new ArticleViewResponse(article));
            }

            return "newArticle";
        }
}
