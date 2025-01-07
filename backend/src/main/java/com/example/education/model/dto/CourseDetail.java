package com.example.education.model.dto;

import com.example.education.model.Course;
import com.example.education.model.Chapter;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseDetail {
    private Course course;
    private List<Chapter> chapters;
    private Integer totalDuration; // 总时长
    private Integer completedChapters; // 已完成章节数
    private Double progress; // 总进度
} 