package com.example.jeon.controller;

import com.example.jeon.domain.Article;
import com.example.jeon.dto.AddArticleRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}