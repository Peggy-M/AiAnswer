package com.example.education.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String subject;
    private String type;  // 课程类型：text, video, live
    private String coverImage;
    private Integer studentCount = 0;
    private Double rating = 0.0;
    private String previewUrl;  // 预览视频URL
    private String previewContent;  // 预览内容
    private Boolean isPublished = false;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Chapter> chapters;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}