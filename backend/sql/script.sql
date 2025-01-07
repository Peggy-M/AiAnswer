create table courses
(
    id          bigint auto_increment
        primary key,
    content     text         null,
    created_at  datetime(6)  null,
    description varchar(255) null,
    title       varchar(255) not null
);

create table exams
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6)  null,
    description varchar(255) null,
    duration    int          null,
    title       varchar(255) null
);

create table questions
(
    id      bigint auto_increment
        primary key,
    answer  text         null,
    content varchar(255) null,
    options text         null,
    score   int          null,
    type    varchar(255) null
);

create table exams_questions
(
    exam_id      bigint not null,
    questions_id bigint not null,
    constraint UK_paibqs45bo7nchvhrtu6w0l7g
        unique (questions_id),
    constraint FKeeqxds9y18pr8ikmnkpowi78l
        foreign key (questions_id) references questions (id),
    constraint FKrn7vrieqs1e8nb1v462b1mjet
        foreign key (exam_id) references exams (id)
);

create table users
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6)  null,
    email      varchar(255) null,
    password   varchar(255) null,
    username   varchar(255) null
);

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


