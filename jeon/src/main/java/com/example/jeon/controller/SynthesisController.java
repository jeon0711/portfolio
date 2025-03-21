package com.example.jeon.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.jeon.dto.SynthesisResponse;
import com.example.jeon.service.SynthesisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/synthesis")
public class SynthesisController {
    private static final Logger logger = LoggerFactory.getLogger(SynthesisController.class);
    private final SynthesisService synthesisService;
    @GetMapping("/")
    public String defaultRoot(Principal principal, Model model) {
        try {
            SynthesisResponse rt = synthesisService.findByEmail(principal.getName()).orElse(null);
            model.addAttribute("synthesis",rt);
            return "synthesis";
        }
        catch(Throwable e)
        { logger.error(e.getMessage());
            return "redirect:/";
        }

    }
    @GetMapping("/{email}")
    public String findByEmail(@PathVariable String email,Model model) {
        try {
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                logger.info(email);
                throw new IllegalArgumentException("잘못된 이메일 형식입니다.");
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && auth.getName().equals(email)) {
                logger.info("synthesis:redirect");
                return "redirect:synthesis/";
            }

            SynthesisResponse rt = synthesisService.findByEmail(email).orElse(null);
            model.addAttribute("synthesis", rt);
            return "synthesis";
        } catch (Throwable e) {
            logger.error(e.getMessage());
            return "redirect:/";
        }

    }
}
