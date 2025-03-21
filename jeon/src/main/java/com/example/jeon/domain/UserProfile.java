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
public class UserProfile extends  BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "author", nullable = false)
    private String author;//email
    @Column(name="name",nullable = true)
    private String name;//이름
    @Column(name="title",nullable = false)
    private String title;
    @Column(name = "content", columnDefinition = "TEXT", nullable = true)
    private String content;
    @Convert(converter = StringListConverter.class) // JSON 변환 적용
    private List<String> skills;
    @Column(name="phone",nullable = true)
    private String phone;
    @Convert(converter = StringListConverter.class) // JSON 변환 적용
    private List<String> externalUrls;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id", referencedColumnName = "id",nullable =true)
    private Image image;
    @OneToOne(mappedBy = "userProfile")
    private User user;

    @Builder
    public UserProfile(String title,String author,String content,String name,List<String> skills,String phone,List<String> externalUrls)
    {
        this.title=title;
        this.content=content;
        this.author=author;
        this.skills=skills;
        this.name=name;
        this.externalUrls=externalUrls;
        this.phone=phone;
    }
    public void update(String title,String content,String name,List<String>skills,String phone,List<String> externalUrls)
    {
        this.title=title;
        this.content=content;
        this.skills=skills;
        this.name=name;
        this.externalUrls=externalUrls;
        this.phone=phone;

    }
    public void addImage(Image image) {//하기전에 비었는지 확인후 안비었으면 이미지 삭제

        this.image = image;

    }

    public void setUser(User user) {
        if (this.user != null && this.user != user) {
            this.user.setUserProfile(null); // 기존 관계 제거
        }
        this.user = user;
        if (user != null && user.getUserProfile() != this) {
            user.setUserProfile(this); // 양방향 관계 설정
        }
    }
}
