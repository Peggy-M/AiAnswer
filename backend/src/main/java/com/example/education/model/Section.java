package com.example.education.model;

import com.example.education.model.enums.SectionType;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "sections")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    private SectionType type; // VIDEO, DOCUMENT, PRACTICE
    
    private String resourceUrl; // 视频或文档URL
    
    private Integer orderNum; // 小节顺序
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    
    private Integer duration; // 视频时长或预计阅读时间（分钟）
} 