package com.example.education.model.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private String content;
    private Long parentId; // 回复的评论ID，如果是主评论则为null
} 