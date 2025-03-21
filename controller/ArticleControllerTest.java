package com.example.jeon.controller;

import com.example.jeon.configure.JwtProperties;
import com.example.jeon.configure.TokenProvider;
import com.example.jeon.domain.Article;
import com.example.jeon.domain.User;
import com.example.jeon.dto.AddArticleRequest;
import com.example.jeon.dto.UpdateArticleRequest;
import com.example.jeon.repository.ArticleRepository;
import com.example.jeon.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

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
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtProperties jwtProperties;

    private User testUser;
    private String token;
    private Article art;
    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        articleRepository.deleteAll();
    }
    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(testUser, testUser.getPassword(), testUser.getAuthorities()));
    }
    @AfterEach
    public void tearDown()
    {
        userRepository.delete(testUser);
        if(art!=null){
        if(articleRepository.findById(art.getId()).isPresent())
        {
            articleRepository.delete(art);
        }
        }
    }
    @DisplayName("addArticle: 글 추가에 성공한다")
    @Test
    public void addArticle() throws Exception
    {
        final String url="/api/articles/";
        final String title="testTitle";
        final String content="testContent";
        final AddArticleRequest userRequest=new AddArticleRequest(title,content,null);
        final String requestBody=objectMapper.writeValueAsString(userRequest);
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        result.andExpect(status().isCreated());
        List<Article> articles=articleRepository.findAll();
        assertThat(articles.getLast().getTitle()).isEqualTo(title);
        assertThat(articles.getLast().getContent()).isEqualTo((content));
        art=articles.getLast();
    }
    @DisplayName("findAllArticles: 글 목록 조회에 성공한다")
    @Test
    public void findAllArticles() throws Exception
    { final String url="/api/articles/";
        final String title="testTitle";
        final String content="testContent";
        final String author=testUser.getUsername();
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).user(testUser).build());

        final ResultActions result= mockMvc.perform(get(url,savedArticle.getId()));

        result.andExpect(status().isOk());
        List<Article> articles=articleRepository.findAll();
        assertThat(articles.getLast().getTitle()).isEqualTo(title);
        assertThat(articles.getLast().getContent()).isEqualTo((content));
        art=articles.getLast();
    }
    @DisplayName("deleteArticle: 글 삭제에 성공한다")
    @Test
    public void deleteArticles() throws Exception
    {
        String url="/api/articles/";
        final String title="deltestTitle";
        final String content="testContent";
        final String author=testUser.getUsername();
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).user(testUser).build());
        url=url+savedArticle.getId().toString();
        mockMvc.perform(delete(url)).andExpect(status().isOk());
        Optional<Article> articles=articleRepository.findById(savedArticle.getId());
        assertThat(articles).isEmpty();
    }
    @DisplayName("updateArticle: 글 수정에 성공한다")
    @Test
    public void updateArticles() throws Exception
    {
        String url="/api/articles/";
        final String title="updatetestTitle";
        final String content="testContent";
        final String author=testUser.getUsername();
        Article savedArticle=articleRepository.save(Article.builder().title(title).content(content).user(testUser).build());
        final String newTitle="updated new title";
        final String newContent="new content";
        UpdateArticleRequest request=new UpdateArticleRequest(newTitle,newContent,null);
        url=url+savedArticle.getId().toString();
        ResultActions result=mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(request)));
        result.andExpect(status().isOk());
        Article article=articleRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
        art=article;
    }
}