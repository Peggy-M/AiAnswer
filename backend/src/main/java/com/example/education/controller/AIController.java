package com.example.education.controller;

import com.example.education.model.AIAnalysis;
import com.example.education.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    @GetMapping("/analyze/{userId}")
    public ResponseEntity<AIAnalysis> analyzePerformance(@PathVariable Long userId) {
        return ResponseEntity.ok(aiService.analyzeUserPerformance(userId));
    }
    
    @GetMapping("/suggestions/{userId}")
    public ResponseEntity<String> getSuggestions(@PathVariable Long userId) {
        return ResponseEntity.ok(aiService.getPersonalizedSuggestions(userId));
    }
} 