package com.example.education.repository;

import com.example.education.model.AIAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, Long> {
    List<AIAnalysis> findByUserId(Long userId);
} 