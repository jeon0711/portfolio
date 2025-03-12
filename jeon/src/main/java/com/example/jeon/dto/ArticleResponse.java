package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleResponse {
    public final String title;
    public final String content;
    public ArticleResponse(Article article)
    {
     this.title=article.getTitle();
     this.content=article.getContent();
    }
}
