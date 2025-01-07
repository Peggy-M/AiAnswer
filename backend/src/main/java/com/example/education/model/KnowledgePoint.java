package com.example.education.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "knowledge_points")
public class KnowledgePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String content;
    private Integer orderNum;
} 