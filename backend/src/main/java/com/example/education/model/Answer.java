package com.example.education.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamRecord examRecord;
    
    private String userAnswer;
    private Boolean isCorrect;
    private Integer score;
} 