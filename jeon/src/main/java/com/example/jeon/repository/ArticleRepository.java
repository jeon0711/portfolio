package com.example.jeon.repository;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {

    List<Article> findAllByAuthor_Id(long uid);
}
