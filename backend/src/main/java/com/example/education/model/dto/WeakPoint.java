package com.example.education.model.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeakPoint {
    private String knowledgePoint;
    private Double correctRate;
    private Integer questionCount;
    private String subject;
    private List<String> relatedPoints; // 相关知识点
    private String recommendedResources; // 推荐学习资源
} 