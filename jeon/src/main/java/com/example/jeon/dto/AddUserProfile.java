package com.example.jeon.dto;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddUserProfile {
    private String title;
    private String content;
    private String author;
    private MultipartFile image;  // 파일 필드 (nullable)

    public UserProfile toEntity() {//image는s3업로드 완료후 단계에서 넣어줘야한다.
        UserProfile userProfile = UserProfile.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        return userProfile;
    }
}
