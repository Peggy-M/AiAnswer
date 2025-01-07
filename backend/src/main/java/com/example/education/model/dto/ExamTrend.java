package com.example.education.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamTrend {
    private LocalDateTime examTime;
    private Double score;
    private String examName;
    private String subject;
    private Integer duration; // 考试时长（分钟）
    private Integer questionCount; // 题目数量
    private Integer correctCount; // 正确题目数
    private String examType; // 考试类型（如：练习、模拟、正式等）
} 