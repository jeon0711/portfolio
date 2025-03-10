package com.example.jeon.controller;


import com.example.jeon.dto.SynthesisResponse;
import com.example.jeon.service.SynthesisService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class SynthesisController {
    private static final Logger logger = LoggerFactory.getLogger(SynthesisController.class);
    private final SynthesisService synthesisService;

    @GetMapping("{id}")
    public ResponseEntity<SynthesisResponse> findByEmail(@PathVariable String id) {
        Optional<SynthesisResponse> rt=synthesisService.findByEmail(id);
        return rt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
