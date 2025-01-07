package com.example.education.repository;

import com.example.education.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions WHERE subject = ?1 ORDER BY RAND() LIMIT ?2", nativeQuery = true)
    List<Question> findRandomQuestionsBySubject(String subject, Integer limit);
} 