package com.example.education.service;

import com.example.education.model.AIAnalysis;
import java.util.List;

public interface AIService {
    AIAnalysis analyzeUserPerformance(Long userId);
    List<String> generateQuestions(String topic, int count);
    String getPersonalizedSuggestions(Long userId);
} 