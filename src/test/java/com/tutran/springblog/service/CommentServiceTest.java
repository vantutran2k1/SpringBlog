package com.tutran.springblog.service;

import com.tutran.springblog.api.entity.Comment;
import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.exception.CommentNotBelongingToPostException;
import com.tutran.springblog.api.mapper.*;
import com.tutran.springblog.api.payload.comment.CommentPartialUpdateRequestDto;
import com.tutran.springblog.api.repository.CategoryRepository;
import com.tutran.springblog.api.repository.CommentRepository;
import com.tutran.springblog.api.repository.PostRepository;
import com.tutran.springblog.api.service.CommentService;
import com.tutran.springblog.api.service.impl.CategoryServiceImpl;
import com.tutran.springblog.api.service.impl.CommentServiceImpl;
import com.tutran.springblog.api.service.impl.PostServiceImpl;
import com.tutran.springblog.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private final PostMapper postMapper = new PostMapperImpl();
    private final CommentMapper commentMapper = new CommentMapperImpl();
    private final CategoryMapper categoryMapper = new CategoryMapperImpl();

    private CommentService commentService;

    private Post post;

    @BeforeEach
    void beforeEach() {
        var categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
        var postService = new PostServiceImpl(postRepository, postMapper, categoryService);
        commentService = new CommentServiceImpl(commentRepository, postService, commentMapper);
        post = RandomGenerator.generateRandomPost();
        post.setId(RandomGenerator.generateRandomId());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
    }

    @Test
    void testCreateCommentSuccessfully() {
        var request = RandomGenerator.generateRandomCommentRequestDto();
        var comment = commentMapper.commentRequestDtoToComment(request);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        var result = commentService.createComment(post.getId(), request);

        assertNotNull(result);
        assertEquals(result.getName(), request.getName());
        assertEquals(result.getEmail(), request.getEmail());
        assertEquals(result.getBody(), request.getBody());
    }

    @Test
    void testGetCommentsByPostIdSuccessful() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "ASC";

        Set<Comment> comments = Set.of(RandomGenerator.generateRandomComment(), RandomGenerator.generateRandomComment());
        post.setComments(comments);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Comment> mockPage = new PageImpl<>(comments.stream().toList(), pageable, comments.size());

        when(commentRepository.findByPostId(post.getId(), pageable)).thenReturn(mockPage);

        var result = commentService.getCommentsByPostId(post.getId(), pageNo, pageSize, sortBy, sortDir);

        assertNotNull(result);
        assertEquals(comments.size(), result.getData().size());
        assertEquals(comments.size(), result.getMeta().getTotalElements());
        assertTrue(result.getMeta().isLast());
    }

    @Test
    void testGetCommentByIdSuccessful() {
        var commentId = RandomGenerator.generateRandomId();
        var comment = RandomGenerator.generateRandomComment();
        comment.setPost(post);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        var result = commentService.getCommentById(post.getId(), commentId);

        assertNotNull(result);
        assertEquals(result.getName(), comment.getName());
        assertEquals(result.getEmail(), comment.getEmail());
        assertEquals(result.getBody(), comment.getBody());
    }

    @Test
    void testGetCommentNotBelongToPostThrowException() {
        var newPost = post.toBuilder().id(RandomGenerator.generateRandomId()).build();
        var comment = RandomGenerator.generateRandomComment();
        var commentId = RandomGenerator.generateRandomId();
        comment.setId(commentId);
        comment.setPost(newPost);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(CommentNotBelongingToPostException.class, () -> commentService.getCommentById(post.getId(), commentId));
    }

    @Test
    void testPatchUpdateNameCommentSuccessfully() {
        var comment = RandomGenerator.generateRandomComment();
        var commentId = RandomGenerator.generateRandomId();
        comment.setId(commentId);
        comment.setPost(post);

        var request = CommentPartialUpdateRequestDto.builder().name(RandomGenerator.generateRandomString()).build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        var result = commentService.patchUpdateCommentById(post.getId(), commentId, request);

        assertNotNull(result);
        assertEquals(result.getName(), request.getName());
        assertEquals(result.getEmail(), comment.getEmail());
        assertEquals(result.getBody(), comment.getBody());
    }

    @Test
    void testUpdateCommentSuccessfully() {
        var comment = RandomGenerator.generateRandomComment();
        var commentId = RandomGenerator.generateRandomId();
        comment.setId(commentId);
        comment.setPost(post);

        var request = RandomGenerator.generateRandomCommentRequestDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);

        var result = commentService.updateCommentById(post.getId(), commentId, request);

        assertNotNull(result);
        assertEquals(result.getName(), request.getName());
        assertEquals(result.getEmail(), request.getEmail());
        assertEquals(result.getBody(), request.getBody());
    }

    @Test
    void testDeleteCommentSuccessfully() {
        var comment = RandomGenerator.generateRandomComment();
        var commentId = RandomGenerator.generateRandomId();
        comment.setId(commentId);
        comment.setPost(post);


        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        var result = commentService.deleteCommentById(post.getId(), commentId);

        assertNotNull(result);
    }
}
