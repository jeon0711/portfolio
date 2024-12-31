package com.example.jeon.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @Column(name = "savedPath",nullable = true)
    private String savedPath;
    @Builder
    public UserProfile(String title,String name,String content,String savedPath)
    {
        this.title=title;
        this.content=content;
        this.name=name;
        this.savedPath=savedPath;
    }
    public void update(String title,String name,String content,String savedPath)
    {
        this.title=title;
        this.content=content;
        this.name=name;
        this.savedPath=savedPath;
    }
}
