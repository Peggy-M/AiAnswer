package com.example.education.controller;

import com.example.education.model.ExamPaper;
import com.example.education.model.ExamRecord;
import com.example.education.model.dto.ExamAnalysis;
import com.example.education.service.ExamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/exams")
@CrossOrigin(origins = "*")
public class ExamController {
    
    @Autowired
    private ExamService examService;
    
    @GetMapping
    public ResponseEntity<List<ExamPaper>> getAllExams() {
        return ResponseEntity.ok(examService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ExamPaper> getExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.findById(id));
    }
    
    @PostMapping
    public ResponseEntity<ExamPaper> createExam(@RequestBody ExamPaper examPaper) {
        log.info("Creating new exam: {}", examPaper.getTitle());
        return ResponseEntity.ok(examService.save(examPaper));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ExamPaper> updateExam(
            @PathVariable Long id,
            @RequestBody ExamPaper examPaper) {
        log.info("Updating exam {}: {}", id, examPaper.getTitle());
        return ResponseEntity.ok(examService.updateExam(id, examPaper));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.info("Deleting exam: {}", id);
        examService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/publish")
    public ResponseEntity<Void> publishExam(@PathVariable Long id) {
        log.info("Publishing exam: {}", id);
        examService.publishExam(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{examId}/start")
    public ResponseEntity<ExamRecord> startExam(
            @PathVariable Long examId,
            @RequestParam Long userId) {
        log.info("User {} starting exam {}", userId, examId);
        return ResponseEntity.ok(examService.startExam(userId, examId));
    }
    
    @PostMapping("/{examId}/submit")
    public ResponseEntity<ExamRecord> submitExam(
            @PathVariable Long examId,
            @RequestParam Long userId,
            @RequestBody Map<Long, String> answers) {
        log.info("User {} submitting exam {}", userId, examId);
        return ResponseEntity.ok(examService.submitExam(userId, examId, answers));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExamRecord>> getUserExamRecords(@PathVariable Long userId) {
        return ResponseEntity.ok(examService.getUserExamRecords(userId));
    }
    
    @PostMapping("/generate")
    public ResponseEntity<ExamPaper> generateRandomPaper(
            @RequestParam String subject,
            @RequestParam Integer questionCount) {
        log.info("Generating random paper for subject: {}, count: {}", subject, questionCount);
        return ResponseEntity.ok(examService.generateRandomPaper(subject, questionCount));
    }
    
    @GetMapping("/records")
    public ResponseEntity<Page<ExamRecord>> getExamRecords(
            @RequestParam(required = false, defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Getting exam records for user: {}", userId);
        return ResponseEntity.ok(examService.getExamRecords(userId, page, size));
    }
    
    @GetMapping("/analysis")
    public ResponseEntity<ExamAnalysis> getExamAnalysis(
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        log.info("Getting exam analysis for user: {}", userId);
        return ResponseEntity.ok(examService.analyzeExamPerformance(userId));
    }
} 