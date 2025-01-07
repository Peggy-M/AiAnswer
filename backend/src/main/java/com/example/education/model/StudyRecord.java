package com.example.education.model;

import com.example.education.model.enums.StudyStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "study_records")
public class StudyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "course_id", insertable = false, updatable = false)
    private Long courseId;

    @Enumerated(EnumType.STRING)
    private StudyStatus status;

    private Integer progress;
    private Integer totalTime;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant lastStudyTime;
} 