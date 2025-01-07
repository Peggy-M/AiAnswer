package com.example.education.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "exam_records")
public class ExamRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamPaper examPaper;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;
    
    private Double score;
    private Boolean isCompleted = false;
    private LocalDateTime startTime;
    private LocalDateTime submitTime;
    private LocalDateTime examTime;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
} 