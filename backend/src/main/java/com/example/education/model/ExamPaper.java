package com.example.education.model;

import com.example.education.model.enums.ExamType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "exam_papers")
public class ExamPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String subject;
    private Integer duration; // 考试时长（分钟）
    private Integer totalScore;
    private Boolean isPublished = false;
    private Integer difficulty; // 使用 difficulty 而不是 difficultyLevel
    
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questions;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
} 