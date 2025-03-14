package com.example.jeon.controller;

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
    @GetMapping()
    public String indexPage()
    {
        return "login";
    }
    @GetMapping("/")
    public String findByEmail(Principal principal, Model model) {
        try {
            SynthesisResponse rt = synthesisService.findByEmail(principal.getName()).orElse(null);
            model.addAttribute("synthesis",rt);
            return "synthesis";
        }
        catch(Throwable e)
        {
            return "login";
        }

    }
    @GetMapping("/{email}")
    public String findByEmail(@PathVariable String email,Model model) {
        try {
            SynthesisResponse rt = synthesisService.findByEmail(email).orElse(null);
           model.addAttribute("synthesis",rt);
           return "synthesis";
        }
        catch(Throwable e)
        {
            return "login";
        }

    }
}
