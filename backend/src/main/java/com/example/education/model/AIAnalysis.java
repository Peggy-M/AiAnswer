package com.example.education.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ai_analysis")
public class AIAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(columnDefinition = "TEXT")
    private String learningBehavior; // 学习行为数据
    
    @Column(columnDefinition = "TEXT")
    private String suggestions; // AI建议
    
    @CreationTimestamp
    private LocalDateTime createdAt;
} 