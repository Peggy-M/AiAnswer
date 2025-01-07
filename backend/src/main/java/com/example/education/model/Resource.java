package com.example.education.model;

import com.example.education.model.enums.ResourceType;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ResourceType type; // 资源类型：VIDEO, IMAGE, DOCUMENT
    
    private String url; // 资源URL
    private String format; // 资源格式
    private Long size; // 文件大小
} 