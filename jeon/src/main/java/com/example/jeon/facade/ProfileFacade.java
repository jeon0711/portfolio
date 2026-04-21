package com.example.jeon.facade;

import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserProfile;
import com.example.jeon.service.ProfileImageService;
import com.example.jeon.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class ProfileFacade {
    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

    @Transactional
    public UserProfile updateProfileWithImage(AddUserProfile input) {
        // 1. 프로필 정보 업데이트 (이미지 제외 로직만 수행)
        try{
        UserProfile profile = profileService.saveProfileInfo(input);

        // 2. 이미지 처리 로직 (ImageService 호출)
        if (input.getImage() != null) {
            profileImageService.saveImage(profile,input.getImage());
        }
            return profile;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}