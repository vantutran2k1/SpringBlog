CREATE TABLE categories
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    deleted     BOOL         NOT NULL
);

ALTER TABLE posts
    ADD COLUMN category_id BIGINT NOT NULL;

ALTER TABLE posts
    ADD CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories (id);
