package com.example.education.service.impl;

import com.example.education.model.AIAnalysis;
import com.example.education.model.User;
import com.example.education.model.StudyRecord;
import com.example.education.model.ExamRecord;
import com.example.education.repository.AIAnalysisRepository;
import com.example.education.repository.ExamRecordRepository;
import com.example.education.repository.StudyRecordRepository;
import com.example.education.repository.UserRepository;
import com.example.education.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
@Slf4j
public class AIServiceImpl implements AIService {
    
    @Autowired
    private AIAnalysisRepository aiAnalysisRepository;
    
    @Autowired
    private ExamRecordRepository examRecordRepository;
    
    @Autowired
    private StudyRecordRepository studyRecordRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public AIAnalysis analyzeUserPerformance(Long userId) {
        // 首先检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        // 获取用户的学习记录
        List<StudyRecord> studyRecords = studyRecordRepository.findByUserId(userId);
        // 获取考试记录
        List<ExamRecord> examRecords = examRecordRepository.findByUserId(userId);
        
        // 分析学习行为
        String learningBehavior = analyzeLearningBehavior(studyRecords);
        // 生成建议
        String suggestions = generateSuggestions(studyRecords, examRecords);
        
        AIAnalysis analysis = new AIAnalysis();
        analysis.setUser(user);
        analysis.setLearningBehavior(learningBehavior);
        analysis.setSuggestions(suggestions);
        
        return aiAnalysisRepository.save(analysis);
    }

    @Override
    public List<String> generateQuestions(String topic, int count) {
        log.info("Generating {} questions for topic: {}", count, topic);
        List<String> questions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            questions.add(String.format("关于%s的第%d个问题", topic, (i + 1)));
        }
        return questions;
    }

    @Override
    public String getPersonalizedSuggestions(Long userId) {
        log.info("Generating personalized suggestions for user: {}", userId);
        List<StudyRecord> studyRecords = studyRecordRepository.findByUserId(userId);
        List<ExamRecord> examRecords = examRecordRepository.findByUserId(userId);
        return generateSuggestions(studyRecords, examRecords);
    }

    private String analyzeLearningBehavior(List<StudyRecord> studyRecords) {
        // 实现学习行为分析逻辑
        StringBuilder analysis = new StringBuilder();
        analysis.append("学习行为分析：\n");
        analysis.append("1. 学习时间分布：主要集中在晚上\n");
        analysis.append("2. 学习持续时间：平均每次2小时\n");
        analysis.append("3. 学习频率：每周3-4次\n");
        return analysis.toString();
    }

    private String generateSuggestions(List<StudyRecord> studyRecords, List<ExamRecord> examRecords) {
        // 实现建议生成逻辑
        StringBuilder suggestions = new StringBuilder();
        suggestions.append("学习建议：\n");
        suggestions.append("1. 增加练习题的数量，巩固知识点\n");
        suggestions.append("2. 建议复习薄弱环节，特别是考试中出错的题目\n");
        suggestions.append("3. 保持规律的学习时间，避免疲劳学习\n");
        suggestions.append("4. 建议适当调整学习计划，增加早晨学习时间\n");
        return suggestions.toString();
    }
} 