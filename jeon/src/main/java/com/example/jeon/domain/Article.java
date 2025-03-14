package com.example.jeon.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "article")
public class Article  extends  BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval =true)
    private List<Image> images = new ArrayList<Image>();


    @Builder
    public Article(User user, String title, String content) {
        this.author=user;
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        if(title!=null){
        this.title = title;}
        if(content!=null){
        this.content = content;
        }
    }
    // 추가 로직: 이미지 추가 메서드
    public void addImage(Image image) {
        images.add(image);
    }
    // Getters and Setters
}
