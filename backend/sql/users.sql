create table users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    email      varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null
);

INSERT INTO education_db.users (created_at, email, password, username) VALUES ('2025-01-07 15:25:33.000000', 'test@example.com', '$2a$10$XXX', 'test_user');
