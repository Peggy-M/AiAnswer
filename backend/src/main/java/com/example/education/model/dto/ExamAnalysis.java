package com.example.education.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnalysis {
    private Integer totalExams;
    private Double averageScore;
    private Integer passedExams;
    private Integer failedExams;
    private Double highestScore;
    private Double lowestScore;
    private Map<String, Double> subjectScores; // 各科目平均分
    private List<ExamTrend> trends; // 成绩趋势
    private Map<String, Integer> questionTypeAnalysis; // 题型分析
    private List<WeakPoint> weakPoints; // 薄弱知识点
    
    // 添加更多分析数据
    private Map<String, Double> monthlyScores; // 月度平均分
    private Map<String, Integer> examTypeDistribution; // 考试类型分布
    private Double recentProgress; // 最近进步幅度
    private Map<String, Double> skillLevels; // 各能力水平评估
}
