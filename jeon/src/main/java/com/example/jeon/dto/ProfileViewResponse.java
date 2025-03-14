package com.example.jeon.dto;

import com.example.jeon.domain.Image;
import com.example.jeon.domain.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@AllArgsConstructor
@Getter
public class ProfileViewResponse {

        private String title;
        private String content;
        private String author;
        private String name;
        private Image image;  // 파일 필드 (nullable)
        private List<String> skills;
    public ProfileViewResponse(UserProfile profile)
    {
        this.title=profile.getTitle();
        this.content= profile.getContent();
        this.author= profile.getAuthor();
        this.image= profile.getImage();
        this.skills=profile.getSkills();
        this.name= profile.getName();

    }
}
