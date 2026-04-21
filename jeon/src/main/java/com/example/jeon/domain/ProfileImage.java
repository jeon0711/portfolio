package com.example.jeon.domain;
import com.example.jeon.util.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ProfileImage extends Image {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile user;
    public void setUserProfile(UserProfile user) {
        this.user = user;
        // 상대방(User) 객체에도 나 자신(ProfileImage)을 심어줌
        if (user != null) {
            user.setProfileImage(this);
        }
    }
}