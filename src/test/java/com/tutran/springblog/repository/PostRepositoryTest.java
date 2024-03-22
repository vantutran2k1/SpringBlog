package com.tutran.springblog.repository;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.utils.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("h2")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Test
    void testCreatePost() {
        var post = RandomGenerator.generateRandomPost();
        var savedPost = entityManager.persistAndFlush(post);

        assertThat(savedPost.getId()).isPositive();
        assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
        assertThat(savedPost.getDescription()).isEqualTo(post.getDescription());
        assertThat(savedPost.getContent()).isEqualTo(post.getContent());
    }

    @Test
    void testGetAllPosts() {
        var post1 = RandomGenerator.generateRandomPost();
        var post2 = RandomGenerator.generateRandomPost();

        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.flush();

        Iterable<Post> posts = postRepository.findAll();

        assertThat(posts).hasSize(2);
    }

    @Test
    void testGetPostById() {
        var post = RandomGenerator.generateRandomPost();
        entityManager.persistAndFlush(post);

        Optional<Post> optionalPost = postRepository.findById(post.getId());

        assertThat(optionalPost).isPresent();
        var retrievedPost = optionalPost.get();
        assertThat(retrievedPost).isEqualTo(post);
    }

    @Test
    void testPartialUpdatePost() {
        var originalPost = RandomGenerator.generateRandomPost();
        entityManager.persistAndFlush(originalPost);

        var updatedPost = originalPost.toBuilder().title(RandomGenerator.generateRandomString()).build();
        entityManager.merge(updatedPost);
        entityManager.flush();

        assertThat(postRepository.findById(originalPost.getId()))
                .isPresent()
                .get()
                .extracting(Post::getTitle, Post::getDescription, Post::getContent)
                .containsExactly(updatedPost.getTitle(), originalPost.getDescription(), originalPost.getContent());
    }

    @Test
    void testUpdatePost() {
        var originalPost = RandomGenerator.generateRandomPost();
        entityManager.persistAndFlush(originalPost);

        var updatedPost = Post.builder()
                .id(originalPost.getId())
                .title(RandomGenerator.generateRandomString())
                .description(RandomGenerator.generateRandomString())
                .content(RandomGenerator.generateRandomString())
                .build();
        postRepository.save(updatedPost);

        assertThat(postRepository.findById(originalPost.getId()))
                .isPresent()
                .get()
                .extracting(Post::getTitle, Post::getDescription, Post::getContent)
                .containsExactly(updatedPost.getTitle(), updatedPost.getDescription(), updatedPost.getContent());
    }

    @Test
    void testDeletePost() {
        var post = RandomGenerator.generateRandomPost();
        entityManager.persistAndFlush(post);

        postRepository.delete(post);

        assertThat(postRepository.findById(post.getId())).isEmpty();
    }
}
