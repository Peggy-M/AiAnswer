-- 初始化用户数据
INSERT INTO users (id, username, password, email, created_at) 
VALUES (1, 'test_user', '$2a$10$XXX', 'test@example.com', NOW())
ON DUPLICATE KEY UPDATE username = VALUES(username);

-- 初始化角色数据
INSERT INTO roles (id, name) 
VALUES (1, 'ROLE_STUDENT')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- 用户角色关联
INSERT INTO user_roles (user_id, roles_id)
VALUES (1, 1)
ON DUPLICATE KEY UPDATE user_id = VALUES(user_id);

-- 确保表存在
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    subject VARCHAR(100),
    type VARCHAR(50),
    cover_image VARCHAR(255),
    student_count INT DEFAULT 0,
    rating DOUBLE DEFAULT 0.0,
    preview_url VARCHAR(255),
    preview_content TEXT,
    is_published BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO courses (title, description, subject, type, cover_image, student_count, rating, is_published)
VALUES 
('Java编程基础', '学习Java编程的基础知识', '编程', 'text', '/images/java.jpg', 100, 4.5, true),
('Python数据分析', '使用Python进行数据分析', '数据科学', 'video', '/images/python.jpg', 80, 4.8, true),
('Web开发实战', '从零开始学习Web开发', '编程', 'live', '/images/web.jpg', 120, 4.6, true)
ON DUPLICATE KEY UPDATE title = VALUES(title); 