package com.example.jeon.controller;
import com.example.jeon.dto.ResumeResponse;
import com.example.jeon.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class ResumeViewController {
    private final ResumeService resumeService;
    @GetMapping("/{id}")
    public String getResume(@PathVariable String id, Model model) {
        Optional<ResumeResponse> rt = resumeService.findByEmail(id);
        return "article";
    }
}
