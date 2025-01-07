package com.example.jeon.domain;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "author", nullable = false)
    private String author;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", referencedColumnName = "id",nullable =true)
    private Image image;
    @OneToOne(mappedBy = "userProfile")
    private User user;

    @Builder
    public UserProfile(String title,String author,String content)
    {
        this.title=title;
        this.content=content;
        this.author=author;
    }
    public void update(String title,String content)
    {
        this.title=title;
        this.content=content;
    }
    public void addImage(Image image) {
        if (this.image != null) {
            this.image.setUserProfile(null); // 기존 이미지를 해제
        }
        this.image = image;
        if (image != null) {
            image.setUserProfile(this);
        }
    }
    public void setUser(User user) {
        if (this.user != null) {
            this.user.setUserProfile(null);
        }
        this.user = user;
        if (user != null) {
            user.setUserProfile(this);
        }
    }
}
