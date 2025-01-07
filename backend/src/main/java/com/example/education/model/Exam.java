package com.example.education.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private Integer duration; // 考试时长（分钟）
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
} 