package com.example.education.service;

import com.example.education.model.ExamPaper;
import com.example.education.model.ExamRecord;
import com.example.education.model.dto.ExamAnalysis;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ExamService {
    List<ExamPaper> findAll();
    ExamPaper findById(Long id);
    ExamPaper save(ExamPaper examPaper);
    ExamPaper updateExam(Long id, ExamPaper examPaper);
    void deleteById(Long id);
    void publishExam(Long id);
    ExamRecord startExam(Long userId, Long examId);
    ExamRecord submitExam(Long userId, Long examId, Map<Long, String> answers);
    List<ExamRecord> getUserExamRecords(Long userId);
    ExamPaper generateRandomPaper(String subject, Integer questionCount);
    Page<ExamRecord> getExamRecords(Long userId, int page, int size);
    ExamAnalysis analyzeExamPerformance(Long userId);
} 