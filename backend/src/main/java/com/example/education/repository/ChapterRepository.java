package com.example.education.repository;

import com.example.education.model.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    List<Chapter> findByCourseId(Long courseId);
    
    Page<Chapter> findByCourseId(Long courseId, Pageable pageable);
    
    // 修改查询语句，使用正确的关联关系
    @Query("SELECT DISTINCT c FROM Chapter c " +
           "INNER JOIN c.course co " +
           "INNER JOIN StudyRecord sr ON sr.courseId = co.id " +
           "WHERE sr.userId = :userId " +
           "AND sr.progress = 100 " +
           "AND co.id = :courseId")
    List<Chapter> findCompletedChaptersByUserIdAndCourseId(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId);
    
    // 获取课程的总章节数
    long countByCourseId(Long courseId);
    
    // 获取章节的顺序号
    @Query("SELECT MAX(c.orderNum) FROM Chapter c WHERE c.course.id = :courseId")
    Integer getMaxOrderNumByCourseId(@Param("courseId") Long courseId);
} 