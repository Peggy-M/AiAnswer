package com.example.education.service;

import com.example.education.model.Course;
import com.example.education.model.Comment;
import com.example.education.model.dto.CourseStatus;
import org.springframework.data.domain.Page;

public interface CourseService {
    Page<Course> findCourses(int page, int size, String type, String subject);
    
    Course findById(Long id);
    
    Course save(Course course);
    
    Course updateCourse(Long id, Course course);
    
    void deleteById(Long id);
    
    Comment addComment(Long courseId, Comment comment);
    
    Page<Comment> getComments(Long courseId, int page, int size);
    
    void joinCourse(Long courseId);
    
    CourseStatus getCourseStatus(Long courseId);
} 