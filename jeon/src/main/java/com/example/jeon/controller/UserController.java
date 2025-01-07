package com.example.jeon.controller;

import com.example.jeon.dto.AddUserRequest;
import com.example.jeon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @PostMapping("/")
    public String singup(AddUserRequest request)
    {
     userService.save(request);
     return "redirect:user/login";
    }
    @GetMapping("/login")
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
    {
        new SecurityContextLogoutHandler().logout(request,response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:user/login";
    }

}
