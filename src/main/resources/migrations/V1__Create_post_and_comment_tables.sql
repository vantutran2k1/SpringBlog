CREATE TABLE posts
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    title         VARCHAR(255)          NOT NULL,
    `description` VARCHAR(255)          NOT NULL,
    content       VARCHAR(255)          NOT NULL,
    deleted       BOOL                  NOT NULL,
    CONSTRAINT pk_posts PRIMARY KEY (id)
);

CREATE TABLE comments
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    email   VARCHAR(255)          NOT NULL,
    body    VARCHAR(255)          NOT NULL,
    deleted BOOL                  NOT NULL,
    post_id BIGINT                NOT NULL,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

ALTER TABLE comments
    ADD CONSTRAINT FK_COMMENTS_ON_POST FOREIGN KEY (post_id) REFERENCES posts (id);