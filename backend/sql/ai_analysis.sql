create table ai_analysis
(
    id                bigint auto_increment
        primary key,
    created_at        datetime(6) null,
    learning_behavior text        null,
    suggestions       text        null,
    user_id           bigint      null,
    constraint FKs1nps0oxfur5f4wwghroj7qix
        foreign key (user_id) references users (id)
);

INSERT INTO education_db.ai_analysis (created_at, learning_behavior, suggestions, user_id) VALUES ('2025-01-07 06:54:37.434585', null, '根据您的学习情况，建议多做练习题，加强知识点巩固。', null);
INSERT INTO education_db.ai_analysis (created_at, learning_behavior, suggestions, user_id) VALUES ('2025-01-07 06:58:12.834206', null, '根据您的学习情况，建议多做练习题，加强知识点巩固。', null);
INSERT INTO education_db.ai_analysis (created_at, learning_behavior, suggestions, user_id) VALUES ('2025-01-07 07:25:45.673624', '学习行为分析：
1. 学习时间分布：主要集中在晚上
2. 学习持续时间：平均每次2小时
3. 学习频率：每周3-4次
', '学习建议：
1. 增加练习题的数量，巩固知识点
2. 建议复习薄弱环节，特别是考试中出错的题目
3. 保持规律的学习时间，避免疲劳学习
4. 建议适当调整学习计划，增加早晨学习时间
', 1);
INSERT INTO education_db.ai_analysis (created_at, learning_behavior, suggestions, user_id) VALUES ('2025-01-07 07:26:30.061710', '学习行为分析：
1. 学习时间分布：主要集中在晚上
2. 学习持续时间：平均每次2小时
3. 学习频率：每周3-4次
', '学习建议：
1. 增加练习题的数量，巩固知识点
2. 建议复习薄弱环节，特别是考试中出错的题目
3. 保持规律的学习时间，避免疲劳学习
4. 建议适当调整学习计划，增加早晨学习时间
', 1);
INSERT INTO education_db.ai_analysis (created_at, learning_behavior, suggestions, user_id) VALUES ('2025-01-07 07:26:31.806125', '学习行为分析：
1. 学习时间分布：主要集中在晚上
2. 学习持续时间：平均每次2小时
3. 学习频率：每周3-4次
', '学习建议：
1. 增加练习题的数量，巩固知识点
2. 建议复习薄弱环节，特别是考试中出错的题目
3. 保持规律的学习时间，避免疲劳学习
4. 建议适当调整学习计划，增加早晨学习时间
', 1);
