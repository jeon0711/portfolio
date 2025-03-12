package com.example.jeon.domain;
import com.example.jeon.util.StringListConverter;
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
    private String author;//이름이다
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="content",nullable = true)
    private String content;
    @Convert(converter = StringListConverter.class) // JSON 변환 적용
    private List<String> skills;
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
    public UserProfile(String title,String author,String content,List<String> skills)
    {
        this.title=title;
        this.content=content;
        this.author=author;
        this.skills=skills;
    }
    public void update(String title,String content,List<String>skills)
    {
        this.title=title;
        this.content=content;
            this.skills=skills;

    }
    public void addImage(Image image) {//하기전에 비었는지 확인후 안비었으면 이미지 삭제

        this.image = image;
        image.setUserProfile(this);

    }
    public void addSkill(String skill) {

        this.skills.add(skill);
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
