package com.example.jeon.controller;

import com.example.jeon.domain.UserProfile;
import com.example.jeon.dto.ProfileViewResponse;
import com.example.jeon.dto.SynthesisResponse;
import com.example.jeon.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @GetMapping("/")
    public String index() {
       return "index";
    }
    @GetMapping("/home/{email}")
    public String findByEmail(@PathVariable String email,Model model) {
        try {
            logger.info("home:email");
            if (email == null || email.isBlank()) { // 빈 값 체크
                return "redirect:/";
            }
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
            }

            return "redirect:synthesis/"+email;
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return "redirect:/";
        }

    }
}
