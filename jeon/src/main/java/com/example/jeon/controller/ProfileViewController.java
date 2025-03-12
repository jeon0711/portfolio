package com.example.jeon.controller;

import com.example.jeon.domain.Article;
import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.ArticleListViewResponse;
import com.example.jeon.dto.ArticleViewResponse;
import com.example.jeon.dto.ProfileViewResponse;
import com.example.jeon.service.ArticleService;
import com.example.jeon.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RequiredArgsConstructor
@Controller
@RequestMapping("/profile")
public class ProfileViewController {

    private final ProfileService profileService;
    private static final Logger logger = LoggerFactory.getLogger(ProfileViewController.class);
    @GetMapping("/{id}")
    public String getProfile(@PathVariable String id, Model model) {
        try {
            UserProfile article = profileService.searchProfile(id);
            model.addAttribute("userProfile", new ProfileViewResponse(article));
            return "/userProfile/userProfile";
        } catch (EntityNotFoundException e) {
            // 프로필을 찾을 수 없는 경우 404 페이지로 이동
            return "error/404";
        } catch (Throwable e) {
            // 기타 예외가 발생하면 에러 페이지로 이동
            logger.error(e.getMessage());
            return "error/500";
        }
    }


    @GetMapping("/update-profile")//애는 principal로 권한확인
    public String getUpdateProfile(Model model, Principal principal) {

        try {
            UserProfile article = profileService.searchProfile(principal.getName());
            logger.info(principal.getName());
            model.addAttribute("userProfile", new ProfileViewResponse(article));
            return "/userProfile/updateProfile";
        } catch (EntityNotFoundException e) {
            // 프로필을 찾을 수 없는 경우 404 페이지로 이동
            return "error/404";
        } catch (Throwable e) {
            // 기타 예외가 발생하면 에러 페이지로 이동
            logger.error(e.getMessage());
            return "error/500";
        }
    }
}
