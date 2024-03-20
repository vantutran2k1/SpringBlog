package com.tutran.springblog.repository;

import com.tutran.springblog.entity.Comment;
import com.tutran.springblog.entity.Post;
import com.tutran.springblog.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private Post post;

    @BeforeEach
    void beforeEach() {
        post = RandomGenerator.generateRandomPost();
        entityManager.persist(post);
    }

    @Test
    void testCreateComment() {
        var comment = RandomGenerator.generateRandomComment(post);
        var savedComment = entityManager.persist(comment);

        entityManager.flush();

        assertThat(savedComment.getId()).isPositive();
        assertThat(savedComment.getName()).isEqualTo(comment.getName());
        assertThat(savedComment.getEmail()).isEqualTo(comment.getEmail());
        assertThat(savedComment.getBody()).isEqualTo(comment.getBody());
        assertThat(savedComment.getPost()).isEqualTo(comment.getPost());
    }

    @Test
    void testGetAllComments() {
        var comment1 = RandomGenerator.generateRandomComment(post);
        var comment2 = RandomGenerator.generateRandomComment(post);

        entityManager.persist(comment1);
        entityManager.persist(comment2);
        entityManager.flush();

        Iterable<Comment> comments = commentRepository.findAll();

        assertThat(comments).hasSize(2);
    }

    @Test
    void testGetCommentById() {
        var comment = RandomGenerator.generateRandomComment(post);
        entityManager.persistAndFlush(comment);

        Optional<Comment> optionalComment = commentRepository.findById(comment.getId());

        assertThat(optionalComment).isPresent();
        var retrievedComment = optionalComment.get();
        assertThat(retrievedComment).isEqualTo(comment);
    }

    @Test
    void testPartialUpdateComment() {
        var originalComment = RandomGenerator.generateRandomComment(post);
        entityManager.persistAndFlush(originalComment);

        var updatedComment = originalComment.toBuilder().name(RandomGenerator.generateRandomString()).build();
        entityManager.merge(updatedComment);
        entityManager.flush();

        assertThat(commentRepository.findById(originalComment.getId()))
                .isPresent()
                .get()
                .extracting(Comment::getName, Comment::getEmail, Comment::getBody, Comment::getPost)
                .containsExactly(updatedComment.getName(), originalComment.getEmail(), originalComment.getBody(), originalComment.getPost());
    }

    @Test
    void testUpdateComment() {
        var originalComment = RandomGenerator.generateRandomComment(post);
        entityManager.persist(originalComment);

        var newPost = RandomGenerator.generateRandomPost();
        entityManager.persist(newPost);

        entityManager.flush();

        var updatedComment = Comment.builder()
                .id(originalComment.getId())
                .name(RandomGenerator.generateRandomString())
                .email(RandomGenerator.generateRandomEmail())
                .body(RandomGenerator.generateRandomString())
                .post(newPost)
                .build();
        commentRepository.save(updatedComment);

        assertThat(commentRepository.findById(originalComment.getId()))
                .isPresent()
                .get()
                .extracting(Comment::getName, Comment::getEmail, Comment::getBody, Comment::getPost)
                .containsExactly(updatedComment.getName(), updatedComment.getEmail(), updatedComment.getBody(), updatedComment.getPost());
    }
}
