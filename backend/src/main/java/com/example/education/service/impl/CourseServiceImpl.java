package com.example.education.service.impl;

import com.example.education.model.Course;
import com.example.education.model.Comment;
import com.example.education.model.StudyRecord;
import com.example.education.model.dto.CourseStatus;
import com.example.education.model.enums.StudyStatus;
import com.example.education.repository.CourseRepository;
import com.example.education.repository.CommentRepository;
import com.example.education.repository.StudyRecordRepository;
import com.example.education.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    @Override
    public Course save(Course course) {
        log.info("Saving course: {}", course.getTitle());
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = findById(id);
        BeanUtils.copyProperties(course, existingCourse, "id", "createdAt", "comments");
        return courseRepository.save(existingCourse);
    }

    @Override
    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public Comment addComment(Long courseId, Comment comment) {
        Course course = findById(courseId);
        comment.setCourse(course);
        return commentRepository.save(comment);
    }

    @Override
    public Page<Comment> getComments(Long courseId, int page, int size) {
        return commentRepository.findByCourseId(courseId, PageRequest.of(page, size));
    }

    @Override
    public Page<Course> findCourses(int page, int size, String type, String subject) {
        log.debug("Finding courses with page={}, size={}, type={}, subject={}", 
                  page, size, type, subject);
                  
        // 验证分页参数
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }
        
        Specification<Course> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 添加已发布条件
            predicates.add(cb.isTrue(root.get("isPublished")));
            
            // 添加类型条件
            if (StringUtils.hasText(type)) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            
            // 添加科目条件
            if (StringUtils.hasText(subject)) {
                predicates.add(cb.equal(root.get("subject"), subject));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        try {
            Page<Course> result = courseRepository.findAll(
                spec, 
                PageRequest.of(page, size, Sort.by("createdAt").descending())
            );
            log.debug("Found {} courses", result.getTotalElements());
            return result;
        } catch (Exception e) {
            log.error("Error finding courses", e);
            throw e;
        }
    }

    @Override
    public void joinCourse(Long courseId) {
        // TODO: 获取当前用户
        Course course = findById(courseId);
        StudyRecord studyRecord = new StudyRecord();
        studyRecord.setCourse(course);
        // studyRecord.setUser(currentUser);
        studyRecord.setStatus(StudyStatus.NOT_STARTED);
        studyRecord.setProgress(0);
        studyRecordRepository.save(studyRecord);
    }

    @Override
    public CourseStatus getCourseStatus(Long courseId) {
        // 获取当前用户ID（这里假设已经有了用户认证系统）
        Long userId = 1L; // 临时硬编码，实际应该从SecurityContext中获取
        
        CourseStatus status = new CourseStatus();
        status.setCourseId(courseId);
        
        try {
            // 使用 Optional 处理可能的空值情况
            studyRecordRepository
                .findFirstByUserIdAndCourseIdOrderByLastStudyTimeDesc(userId, courseId)
                .ifPresent(record -> {
                    status.setStatus(record.getStatus());
                    status.setProgress(record.getProgress());
                    status.setLastStudyTime(record.getLastStudyTime().toEpochMilli());
                    status.setTotalTime(record.getTotalTime());
                    status.setIsJoined(true);
                });
            
            return status;
        } catch (Exception e) {
            log.error("Error getting course status for courseId: " + courseId, e);
            return status; // 返回默认状态
        }
    }
} 