package com.example.education.model.dto;

import lombok.Data;

@Data
public class ProgressRequest {
    private Integer progress; // 0-100
    private Integer duration; // 本次学习时长（秒）
    private String note; // 学习笔记
} 