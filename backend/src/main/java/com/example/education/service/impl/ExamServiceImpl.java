package com.example.education.service.impl;

import com.example.education.model.ExamPaper;
import com.example.education.model.ExamRecord;
import com.example.education.model.Question;
import com.example.education.model.User;
import com.example.education.model.Answer;
import com.example.education.model.dto.ExamAnalysis;
import com.example.education.model.dto.ExamTrend;
import com.example.education.model.dto.WeakPoint;
import com.example.education.repository.ExamPaperRepository;
import com.example.education.repository.ExamRecordRepository;
import com.example.education.repository.QuestionRepository;
import com.example.education.service.ExamService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.Collections;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@Transactional
public class ExamServiceImpl implements ExamService {
    
    @Autowired
    private ExamPaperRepository examPaperRepository;
    
    @Autowired
    private ExamRecordRepository examRecordRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Override
    public List<ExamPaper> findAll() {
        return examPaperRepository.findAll();
    }

    @Override
    public ExamPaper findById(Long id) {
        return examPaperRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam paper not found"));
    }

    @Override
    public ExamPaper save(ExamPaper examPaper) {
        log.info("Saving exam paper: {}", examPaper.getTitle());
        return examPaperRepository.save(examPaper);
    }

    @Override
    public ExamPaper updateExam(Long id, ExamPaper examPaper) {
        ExamPaper existingExam = findById(id);
        BeanUtils.copyProperties(examPaper, existingExam, "id", "createdAt");
        return examPaperRepository.save(existingExam);
    }

    @Override
    public void deleteById(Long id) {
        examPaperRepository.deleteById(id);
    }

    @Override
    public void publishExam(Long id) {
        ExamPaper examPaper = findById(id);
        examPaper.setIsPublished(true);
        examPaperRepository.save(examPaper);
    }

    @Override
    public ExamRecord startExam(Long userId, Long examId) {
        ExamPaper examPaper = findById(examId);
        if (!examPaper.getIsPublished()) {
            throw new RuntimeException("This exam is not published yet");
        }

        ExamRecord record = new ExamRecord();
        User user = new User();
        user.setId(userId);
        record.setUser(user);
        record.setExamPaper(examPaper);
        record.setStartTime(LocalDateTime.now());
        record.setIsCompleted(false);
        
        return examRecordRepository.save(record);
    }

    @Override
    public ExamRecord submitExam(Long userId, Long examId, Map<Long, String> answers) {
        ExamRecord record = examRecordRepository.findByUserIdAndExamPaperId(userId, examId)
                .orElseThrow(() -> new RuntimeException("Exam record not found"));
        
        if (record.getIsCompleted()) {
            throw new RuntimeException("This exam has already been submitted");
        }

        // 创建答案记录
        List<Answer> answerList = answers.entrySet().stream()
                .map(entry -> {
                    Answer answer = new Answer();
                    Question question = record.getExamPaper().getQuestions().stream()
                            .filter(q -> q.getId().equals(entry.getKey()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Question not found"));
                    
                    answer.setQuestion(question);
                    answer.setExamRecord(record);
                    answer.setUserAnswer(entry.getValue());
                    answer.setIsCorrect(entry.getValue().equals(question.getCorrectAnswer()));
                    answer.setScore(answer.getIsCorrect() ? question.getScore() : 0);
                    return answer;
                })
                .collect(Collectors.toList());
        
        record.setAnswers(answerList);
        record.setSubmitTime(LocalDateTime.now());
        record.setIsCompleted(true);
        record.setScore(answerList.stream()
                .mapToDouble(Answer::getScore)
                .sum());
        
        return examRecordRepository.save(record);
    }

    @Override
    public List<ExamRecord> getUserExamRecords(Long userId) {
        return examRecordRepository.findByUserIdOrderBySubmitTimeDesc(userId);
    }

    @Override
    public ExamPaper generateRandomPaper(String subject, Integer questionCount) {
        List<Question> questions = questionRepository.findRandomQuestionsBySubject(subject, questionCount);
        if (questions.size() < questionCount) {
            throw new RuntimeException("Not enough questions available");
        }

        ExamPaper examPaper = new ExamPaper();
        examPaper.setTitle("Random Paper - " + subject);
        examPaper.setQuestions(questions);
        examPaper.setTotalScore(calculateTotalScore(questions));
        examPaper.setIsPublished(false);
        
        return examPaperRepository.save(examPaper);
    }

    private Integer calculateScore(ExamPaper examPaper, Map<Long, String> answers) {
        // 实现评分逻辑
        int totalScore = 0;
        for (Question question : examPaper.getQuestions()) {
            String userAnswer = answers.get(question.getId());
            if (userAnswer != null && userAnswer.equals(question.getCorrectAnswer())) {
                totalScore += question.getScore();
            }
        }
        return totalScore;
    }

    private Integer calculateTotalScore(List<Question> questions) {
        return questions.stream()
                .mapToInt(Question::getScore)
                .sum();
    }

    @Override
    public Page<ExamRecord> getExamRecords(Long userId, int page, int size) {
        return examRecordRepository.findByUserIdOrderByExamTimeDesc(
                userId, PageRequest.of(page, size));
    }
    
    @Override
    public ExamAnalysis analyzeExamPerformance(Long userId) {
        List<ExamRecord> records = examRecordRepository.findByUserId(userId);
        
        if (records.isEmpty()) {
            return createEmptyAnalysis();
        }
        
        // 使用 Stream API 计算统计数据
        DoubleSummaryStatistics stats = records.stream()
                .mapToDouble(record -> record.getScore() != null ? record.getScore() : 0.0)
                .summaryStatistics();
                
        // 计算通过和失败的考试数量
        long passedCount = records.stream()
                .filter(r -> r.getScore() != null && r.getScore() >= 60)
                .count();
                
        // 按科目分组计算平均分
        Map<String, Double> subjectScores = records.stream()
                .filter(r -> r.getScore() != null)
                .collect(Collectors.groupingBy(
                    r -> r.getExamPaper().getSubject(),
                    Collectors.averagingDouble(ExamRecord::getScore)
                ));
                
        // 构建成绩趋势
        List<ExamTrend> trends = records.stream()
                .filter(r -> r.getScore() != null)
                .sorted(Comparator.comparing(ExamRecord::getExamTime))
                .map(r -> ExamTrend.builder()
                        .examTime(r.getExamTime())
                        .score(r.getScore())
                        .examName(r.getExamPaper().getTitle())
                        .subject(r.getExamPaper().getSubject())
                        .duration(r.getExamPaper().getDuration())
                        .questionCount(r.getExamPaper().getQuestions().size())
                        .correctCount(calculateCorrectCount(r))
                        .examType(r.getExamPaper().getExamType().toString())
                        .build())
                .collect(Collectors.toList());
                
        return ExamAnalysis.builder()
                .totalExams(records.size())
                .averageScore(stats.getAverage())
                .passedExams((int) passedCount)
                .failedExams(records.size() - (int) passedCount)
                .highestScore(stats.getMax())
                .lowestScore(stats.getMin())
                .subjectScores(subjectScores)
                .trends(trends)
                .questionTypeAnalysis(analyzeQuestionTypes(records))
                .weakPoints(analyzeWeakPoints(records))
                .monthlyScores(calculateMonthlyScores(records))
                .examTypeDistribution(calculateExamTypeDistribution(records))
                .recentProgress(calculateRecentProgress(records))
                .skillLevels(calculateSkillLevels(records))
                .build();
    }
    
    private Map<String, Integer> analyzeQuestionTypes(List<ExamRecord> records) {
        // 实现题型分析逻辑
        return records.stream()
                .flatMap(r -> r.getAnswers().stream())
                .collect(Collectors.groupingBy(
                    a -> a.getQuestion().getType().toString(),
                    Collectors.summingInt(a -> a.getIsCorrect() ? 1 : 0)
                ));
    }
    
    private List<WeakPoint> analyzeWeakPoints(List<ExamRecord> records) {
        // 实现薄弱知识点分析逻辑
        Map<String, List<Answer>> pointAnswers = records.stream()
                .flatMap(r -> r.getAnswers().stream())
                .collect(Collectors.groupingBy(
                    a -> a.getQuestion().getKnowledgePoint()
                ));
                
        return pointAnswers.entrySet().stream()
                .map(e -> {
                    List<Answer> answers = e.getValue();
                    long correctCount = answers.stream()
                            .filter(Answer::getIsCorrect)
                            .count();
                    double correctRate = (double) correctCount / answers.size();
                    
                    return WeakPoint.builder()
                            .knowledgePoint(e.getKey())
                            .correctRate(correctRate)
                            .questionCount(answers.size())
                            .build();
                })
                .filter(wp -> wp.getCorrectRate() < 0.6) // 正确率低于60%的知识点
                .sorted(Comparator.comparing(WeakPoint::getCorrectRate))
                .collect(Collectors.toList());
    }
    
    private ExamAnalysis createEmptyAnalysis() {
        return ExamAnalysis.builder()
                .totalExams(0)
                .averageScore(0.0)
                .passedExams(0)
                .failedExams(0)
                .highestScore(0.0)
                .lowestScore(0.0)
                .subjectScores(Collections.emptyMap())
                .trends(Collections.emptyList())
                .questionTypeAnalysis(Collections.emptyMap())
                .weakPoints(Collections.emptyList())
                .build();
    }

    private int calculateCorrectCount(ExamRecord record) {
        return (int) record.getAnswers().stream()
                .filter(Answer::getIsCorrect)
                .count();
    }

    private boolean isAnswerCorrect(Answer answer) {
        return answer != null && 
               answer.getIsCorrect() != null && 
               answer.getIsCorrect() && 
               answer.getQuestion() != null && 
               answer.getQuestion().getCorrectAnswer() != null;
    }

    // 计算月度平均分
    private Map<String, Double> calculateMonthlyScores(List<ExamRecord> records) {
        return records.stream()
                .filter(r -> r.getScore() != null)
                .collect(Collectors.groupingBy(
                    r -> r.getExamTime().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    Collectors.averagingDouble(ExamRecord::getScore)
                ));
    }

    // 计算考试类型分布
    private Map<String, Integer> calculateExamTypeDistribution(List<ExamRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                    r -> r.getExamPaper().getExamType().toString(),
                    Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    // 计算最近进步幅度
    private Double calculateRecentProgress(List<ExamRecord> records) {
        if (records.size() < 2) return 0.0;
        
        List<ExamRecord> sortedRecords = records.stream()
                .filter(r -> r.getScore() != null)
                .sorted(Comparator.comparing(ExamRecord::getExamTime))
                .collect(Collectors.toList());
                
        int lastIndex = sortedRecords.size() - 1;
        double recentScore = sortedRecords.get(lastIndex).getScore();
        double previousScore = sortedRecords.get(lastIndex - 1).getScore();
        
        return recentScore - previousScore;
    }

    // 计算能力水平评估
    private Map<String, Double> calculateSkillLevels(List<ExamRecord> records) {
        return records.stream()
                .flatMap(r -> r.getAnswers().stream())
                .collect(Collectors.groupingBy(
                    a -> a.getQuestion().getKnowledgePoint(),
                    Collectors.averagingDouble(a -> a.getIsCorrect() ? 1.0 : 0.0)
                ));
    }
} 