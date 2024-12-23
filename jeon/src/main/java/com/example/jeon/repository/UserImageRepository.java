package com.example.jeon.repository;

import com.example.jeon.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserImageRepository extends JpaRepository<UserProfile,Long> {
}
