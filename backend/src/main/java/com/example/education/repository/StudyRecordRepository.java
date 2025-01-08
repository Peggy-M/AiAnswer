package com.example.education.repository;

import com.example.education.model.StudyRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, Long> {
    // 获取最新的学习记录
    Optional<StudyRecord> findFirstByUserIdAndCourseIdOrderByLastStudyTimeDesc(Long userId, Long courseId);
    
    // 根据用户ID查找所有学习记录
    List<StudyRecord> findByUserId(Long userId);
    
    // 根据用户ID查找学习记录并分页
    Page<StudyRecord> findByUserId(Long userId, Pageable pageable);
    
    // 查找用户最近的学习记录
    @Query("SELECT sr FROM StudyRecord sr WHERE sr.user.id = :userId ORDER BY sr.lastStudyTime DESC")
    List<StudyRecord> findRecentByUserId(@Param("userId") Long userId, Pageable pageable);
    
    // 查找用户已完成的学习记录
    @Query("SELECT sr FROM StudyRecord sr WHERE sr.user.id = :userId AND sr.progress = 100")
    List<StudyRecord> findCompletedByUserId(@Param("userId") Long userId);
    
    // 统计用户的学习记录数
    long countByUserId(Long userId);
    
    // 根据用户ID和课程ID查找学习记录
    Optional<StudyRecord> findByUserIdAndCourseId(Long userId, Long courseId);
} 