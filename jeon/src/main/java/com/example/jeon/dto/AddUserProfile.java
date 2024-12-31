package com.example.jeon.dto;

import com.example.jeon.domain.Article;
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
    private String name;
    private MultipartFile image;
    public UserProfile toEntity() {
        return UserProfile.builder().title(title).content(content).name(name).build();
    }
}
