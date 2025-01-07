package com.example.education.repository;

import com.example.education.model.ExamRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRecordRepository extends JpaRepository<ExamRecord, Long> {
    Page<ExamRecord> findByUserIdOrderByExamTimeDesc(Long userId, Pageable pageable);
    List<ExamRecord> findByUserId(Long userId);
    Optional<ExamRecord> findByUserIdAndExamPaperId(Long userId, Long examId);
    List<ExamRecord> findByUserIdOrderBySubmitTimeDesc(Long userId);
    
    @Query("SELECT er FROM ExamRecord er " +
           "WHERE er.user.id = :userId AND er.examTime BETWEEN :startTime AND :endTime")
    List<ExamRecord> findByUserIdAndTimePeriod(
            @Param("userId") Long userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
} 