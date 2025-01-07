package com.example.education.model.enums;

public enum StudyStatus {
    NOT_STARTED("未开始"),
    IN_PROGRESS("学习中"),
    COMPLETED("已完成");

    private final String description;

    StudyStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
} 