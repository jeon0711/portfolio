package com.example.jeon.repository;

import com.example.jeon.domain.User;
import com.example.jeon.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    Optional<UserProfile> findByAuthor(String email);
}
