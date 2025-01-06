package com.example.jeon.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;
    @Column(name="writer",updatable = false)
    private String name;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @Builder
    public Article(String title,String content,String name)
    {
        this.title=title;
        this.content=content;
        this.name=name;
    }
    public void update(String title,String content,String name)
    {
        this.title=title;
        this.content=content;
        this.name=name;
    }
    // Getters and Setters
}
