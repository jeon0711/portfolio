package com.example.jeon.repository;

import com.example.jeon.domain.ArticleImage;
import com.example.jeon.domain.Image;
import com.example.jeon.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {
}
