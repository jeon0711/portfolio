package com.example.jeon.service;

import com.example.jeon.domain.User;
import com.example.jeon.dto.AddUserRequest;
import com.example.jeon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public User loadUserByUsername(String email)
    {
        return userRepository.findByEmail(email).orElseThrow(()->new IllegalArgumentException((email)));
    }
    public Long save(AddUserRequest dto)
    {
        return userRepository.save(User.builder().email(dto.getEmail()).password(dto.getPassword()).build()).getId();
    }
}
