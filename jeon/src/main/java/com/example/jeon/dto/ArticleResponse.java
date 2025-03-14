package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ArticleResponse {
    public final String title;
    public final String content;
    public final List<Image> images;
    public ArticleResponse(Article article)
    {
     this.title=article.getTitle();
     this.content=article.getContent();
     this.images=article.getImages();
    }
}
