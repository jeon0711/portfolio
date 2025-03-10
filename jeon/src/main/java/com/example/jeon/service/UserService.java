package com.example.jeon.service;

import com.example.jeon.domain.User;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.AddUserRequest;
import com.example.jeon.repository.UserProfileRepository;
import com.example.jeon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService  {
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public Long save(AddUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user=User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build();
        UserProfile temp=UserProfile.builder().title(user.getEmail()).content(null).author(user.getEmail()).build();
       temp.setUser(user);
       user.setUserProfile(temp);
       userProfileRepository.save(temp);
       userRepository.save(user);
       return user.getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}
