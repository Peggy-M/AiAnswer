package com.example.education.controller;

import com.example.education.model.Course;
import com.example.education.model.Comment;
import com.example.education.model.dto.CourseStatus;
import com.example.education.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String subject) {
        try {
            log.info("Getting courses with params: page={}, size={}, type={}, subject={}", 
                     page, size, type, subject);
                     
            Page<Course> courses = courseService.findCourses(page, size, type, subject);
            log.info("Found {} courses", courses.getTotalElements());
            
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error getting courses", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        log.info("Creating new course: {}", course.getTitle());
        return ResponseEntity.ok(courseService.save(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        log.info("Updating course {}: {}", id, course.getTitle());
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        log.info("Deleting course with id: {}", id);
        
        if (id == null || "undefined".equals(id)) {
            throw new IllegalArgumentException("Course ID cannot be null or undefined");
        }
        
        try {
            Long courseId = Long.parseLong(id);
            courseService.deleteById(courseId);
            return ResponseEntity.ok().build();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid course ID format: " + id);
        }
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long id,
            @RequestBody Comment comment) {
        log.info("Adding comment to course {}", id);
        return ResponseEntity.ok(courseService.addComment(id, comment));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Page<Comment>> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(courseService.getComments(id, page, size));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinCourse(@PathVariable Long id) {
        log.info("User joining course: {}", id);
        courseService.joinCourse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<CourseStatus> getCourseStatus(@PathVariable Long id) {
        try {
            log.info("Getting course status: {}", id);
            return ResponseEntity.ok(courseService.getCourseStatus(id));
        } catch (Exception e) {
            log.error("Error getting course status", e);
            CourseStatus defaultStatus = new CourseStatus();
            defaultStatus.setCourseId(id);
            return ResponseEntity.ok(defaultStatus);
        }
    }
} 