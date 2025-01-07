package com.example.education.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseProgress {
    private Long courseId;
    private String courseTitle;
    private String coverImage;
    private Integer totalChapters;
    private Integer completedChapters;
    private Double progress;
    private LocalDateTime lastStudyTime;
    
    // 添加额外的有用信息
    private String subject;
    private Integer level;
    private Integer totalDuration; // 总时长（分钟）
    private Integer studiedDuration; // 已学习时长（分钟）
} 