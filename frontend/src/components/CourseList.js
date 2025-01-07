import React, { useState, useEffect } from 'react';
import axios from 'axios';

function CourseList() {
    const [courses, setCourses] = useState([]);

    useEffect(() => {
        const fetchCourses = async () => {
            const response = await axios.get('http://localhost:5000/api/courses');
            setCourses(response.data);
        };
        fetchCourses();
    }, []);

    return (
        <div className="course-list">
            <h2>Available Courses</h2>
            {courses.map(course => (
                <div key={course._id} className="course-item">
                    <h3>{course.title}</h3>
                    {/* 课程内容展示 */}
                </div>
            ))}
        </div>
    );
}

export default CourseList; 