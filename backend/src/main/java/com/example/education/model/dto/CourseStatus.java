package com.example.education.model.dto;

import com.example.education.model.enums.StudyStatus;
import lombok.Data;

@Data
public class CourseStatus {
    private Long courseId;
    private StudyStatus status;  // 学习状态：NOT_STARTED, IN_PROGRESS, COMPLETED
    private Integer progress;    // 学习进度（百分比）
    private Long lastStudyTime;  // 最后学习时间
    private Integer totalTime;   // 总学习时长（分钟）
    private Boolean isJoined;    // 是否已加入课程
    
    // 构造方法
    public CourseStatus() {
        this.status = StudyStatus.NOT_STARTED;
        this.progress = 0;
        this.totalTime = 0;
        this.isJoined = false;
    }
} 