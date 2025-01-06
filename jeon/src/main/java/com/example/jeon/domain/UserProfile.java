package com.example.jeon.domain;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",updatable = false)
    private Long id;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @Column(name = "savedPath",nullable = true)
    private String savedPath;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Builder
    public UserProfile(String title,String author,String content,String savedPath)
    {
        this.title=title;
        this.content=content;
        this.author=author;
        this.savedPath=savedPath;
    }
    public void update(String title,String content,String savedPath)
    {
        this.title=title;
        this.content=content;
        this.savedPath=savedPath;
    }
}
