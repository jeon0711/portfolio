package com.example.jeon.repository;

import com.example.jeon.domain.ArticleImage;
import com.example.jeon.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleImageRepository extends JpaRepository<ArticleImage,Long> {
}
