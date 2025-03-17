package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddUserProfile {
    private String title;
    private String content;
    private String author;
    private MultipartFile image;  // 파일 필드 (nullable)
    private String name;
    private String phone;
    private List<String> skills;
    private List<String> externalUrls;
    public UserProfile toEntity() {//image는s3업로드 완료후 단계에서 넣어줘야한다.skills도 따로 처리해줘야
        UserProfile userProfile = UserProfile.builder()
                .title(title)
                .content(content)
                .author(author).skills(skills).name(name).phone(phone).externalUrls(externalUrls)
                .build();

        return userProfile;
    }
    public MultipartFile getImage() {
        return (image == null || image.isEmpty()) ? null : image;
    }
}
