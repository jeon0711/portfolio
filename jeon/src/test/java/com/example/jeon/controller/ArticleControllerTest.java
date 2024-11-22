package com.example.jeon.controller;

import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    ArticleRepository articleRepository;

    @BeforeEach
    public void setMockMvc()
    {
        this.mockMvc= MockMvcBuilders.webAppContextSetup(context).build();
        articleRepository.deleteAll();
    }

    @DisplayName("addArticle: 글 추가에 성공한다")
    @Test
    public void addArticle() throws Exception
    {
        final String url="/articles";
        final String title="testTitle";
        final String content="testContent";
        final AddArticleRequest userRequest=new AddArticleRequest(title,content);

        final String requestBody=objectMapper.writeValueAsString(userRequest);

        ResultActions result= mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody));

        result.andExpect(status().isCreated());
        List<Article> articles=articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.getFirst().getTitle()).isEqualTo(title);
        assertThat(articles.getFirst().getContent()).isEqualTo((content));
    }
    @DisplayName("findAllArticles: 글 목록 조회에 성공한다")
    @Test
    public void findAllArticles() throws Exception
    {final String url="/articles";
        final String title="testTitle";
        final String content="testContent";
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).build());

        final ResultActions result= mockMvc.perform(get(url,savedArticle.getId()));

        result.andExpect(status().isCreated());
        List<Article> articles=articleRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.getFirst().getTitle()).isEqualTo(title);
        assertThat(articles.getFirst().getContent()).isEqualTo((content));
    }
    @DisplayName("deleteArticle: 글 삭제에 성공한다")
    @Test
    public void deleteArticles() throws Exception
    {
        final String url="/articles";
        final String title="testTitle";
        final String content="testContent";
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).build());
        mockMvc.perform(delete(url,savedArticle.getId())).andExpect(status().isOk());
        List<Article> articles=articleRepository.findAll();
        assertThat(articles).isEmpty();
    }
    @DisplayName("updateArticle: 글 수정에 성공한다")
    @Test
    public void updateArticles() throws Exception
    {
        final String url="/articles";
        final String title="testTitle";
        final String content="testContent";
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).build());
        final String newTitle="new title";
        final String newContent="new content";
        UpdateArticleRequest request=new UpdateArticleRequest(newTitle,newContent);
        ResultActions result=mockMvc.perform(put(url,savedArticle.getId()).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isOk());
        Article article=articleRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
        
    }
}