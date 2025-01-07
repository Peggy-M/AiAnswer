package com.example.education.repository;

import com.example.education.model.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamPaperRepository extends JpaRepository<ExamPaper, Long> {
    List<ExamPaper> findByIsPublishedTrue();
    List<ExamPaper> findByTitleContainingIgnoreCase(String keyword);
    
    // 根据科目查询试卷
    List<ExamPaper> findBySubjectAndIsPublishedTrue(String subject);
    
    // 根据难度等级查询试卷
    List<ExamPaper> findByDifficultyAndIsPublishedTrue(Integer difficulty);
    
    // 根据创建时间倒序查询
    List<ExamPaper> findByIsPublishedTrueOrderByCreatedAtDesc();
    
    // 修改随机查询方法，使用原生 SQL
    @Query(value = "SELECT * FROM exam_papers WHERE subject = :subject AND is_published = true ORDER BY RAND() LIMIT :count", 
           nativeQuery = true)
    List<ExamPaper> findRandomPapersBySubject(@Param("subject") String subject, @Param("count") Integer count);
} 