package com.example.education.model;

import com.example.education.model.enums.QuestionType;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @ElementCollection
    private List<String> options; // 选项列表
    
    private String correctAnswer; // 正确答案
    
    @Column(columnDefinition = "TEXT")
    private String analysis; // 题目解析
    
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    
    private String subject;
    private String knowledgePoint;
    private Integer score;
    private Integer difficulty; // 1-5
    
    @ManyToOne(fetch = FetchType.LAZY)
    private ExamPaper examPaper;
}
