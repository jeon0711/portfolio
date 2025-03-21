package com.example.jeon.controller;

import com.example.jeon.dto.AddUserRequest;
import com.example.jeon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping("/signup")
    public String singup(AddUserRequest request)
    {
        try{
     userService.save(request);
     logger.info("회원가입 성공");
     return "redirect:user/login";} catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/login")//post는 스프링 security제공
    public String login()
    {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup()
    {
        return "user/signup";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response)
    {try {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        logger.info("로그아웃 성공");
        return "redirect:user/login";
    } catch (Exception e) {
        logger.error(e.getMessage());
        throw new RuntimeException(e);
    }
    }

}
