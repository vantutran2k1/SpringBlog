package com.tutran.springblog.service;

import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.mapper.CategoryMapper;
import com.tutran.springblog.api.mapper.CategoryMapperImpl;
import com.tutran.springblog.api.mapper.PostMapper;
import com.tutran.springblog.api.mapper.PostMapperImpl;
import com.tutran.springblog.api.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.api.payload.post.PostPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;
import com.tutran.springblog.api.repository.CategoryRepository;
import com.tutran.springblog.api.repository.PostRepository;
import com.tutran.springblog.api.service.PostService;
import com.tutran.springblog.api.service.impl.CategoryServiceImpl;
import com.tutran.springblog.api.service.impl.PostServiceImpl;
import com.tutran.springblog.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private final PostMapper postMapper = new PostMapperImpl();
    private final CategoryMapper categoryMapper = new CategoryMapperImpl();

    private PostService postService;

    @BeforeEach
    void beforeEach() {
        var categoryService = new CategoryServiceImpl(categoryRepository, categoryMapper);
        postService = new PostServiceImpl(postRepository, postMapper, categoryService);
    }

    @Test
    void testCreatePostSuccessfully() {
        var request = RandomGenerator.generateRandomPostRequestDto();
        var post = postMapper.postRequestDtoToPost(request);
        var category = RandomGenerator.generateRandomCategory();
        post.setCategory(category);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

        var result = postService.createPost(request);

        assertNotNull(result);
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getContent(), result.getContent());
    }

    @Test
    void testCreatePostWithDuplicateTitleShouldThrowException() {
        var request = RandomGenerator.generateRandomPostRequestDto();

        when(postRepository.existsByTitle(any(String.class))).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> postService.createPost(request));
    }

    @Test
    void testGetAllPosts() {
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "title";
        String sortDir = "ASC";

        var category = RandomGenerator.generateRandomCategory();
        var post1 = RandomGenerator.generateRandomPostWithAssociatedCategory(category);
        var post2 = RandomGenerator.generateRandomPostWithAssociatedCategory(category);
        List<Post> posts = List.of(post1, post2);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Post> mockPage = new PageImpl<>(posts, pageable, posts.size());

        when(postRepository.findAll(pageable)).thenReturn(mockPage);

        ResponseDtoWithMeta<List<PostResponseDto>> result = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir, null);

        assertNotNull(result);
        assertEquals(posts.size(), result.getData().size());
        assertEquals(posts.size(), result.getMeta().getTotalElements());
        assertTrue(result.getMeta().isLast());
    }

    @Test
    void testGetPostByIdSuccessfully() {
        var post = RandomGenerator.generateRandomPostWithComments();
        var category = RandomGenerator.generateRandomCategory();
        post.setCategory(category);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        var result = postService.getPostById(post.getId());

        assertNotNull(result);
        assertEquals(result.getId(), post.getId());
        assertEquals(result.getTitle(), post.getTitle());
        assertEquals(result.getDescription(), post.getDescription());
        assertEquals(result.getContent(), post.getContent());
        assertEquals(result.getComments().size(), post.getComments().size());
    }

    @Test
    void testPatchUpdateTitlePostSuccessfully() {
        var category = RandomGenerator.generateRandomCategory();
        var post = RandomGenerator.generateRandomPostWithAssociatedCategory(category);
        post.setId(RandomGenerator.generateRandomId());

        var request = PostPartialUpdateRequestDto.builder().title(RandomGenerator.generateRandomString()).build();
        var updatedPost = post.toBuilder().title(request.getTitle()).build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        var result = postService.patchUpdatePostById(post.getId(), request);

        assertNotNull(result);
        assertEquals(result.getId(), updatedPost.getId());
        assertEquals(result.getTitle(), updatedPost.getTitle());
        assertEquals(result.getDescription(), updatedPost.getDescription());
        assertEquals(result.getContent(), updatedPost.getContent());
    }

    @Test
    void testPatchUpdateTitleDuplicateThrowException() {
        var post = RandomGenerator.generateRandomPost();
        post.setId(RandomGenerator.generateRandomId());

        var updateRequest = PostPartialUpdateRequestDto.builder().title(RandomGenerator.generateRandomString()).build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.existsByTitle(updateRequest.getTitle())).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> postService.patchUpdatePostById(post.getId(), updateRequest));
    }

    @Test
    void testUpdatePostSuccessfully() {
        var category = RandomGenerator.generateRandomCategory();
        var post = RandomGenerator.generateRandomPostWithAssociatedCategory(category);
        post.setId(RandomGenerator.generateRandomId());

        var request = RandomGenerator.generateRandomPostRequestDto();
        var updatedPost = postMapper.postRequestDtoToPost(request);
        updatedPost.setCategory(category);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        var result = postService.updatePostById(post.getId(), request);

        assertNotNull(result);
        assertEquals(result.getId(), updatedPost.getId());
        assertEquals(result.getTitle(), updatedPost.getTitle());
        assertEquals(result.getDescription(), updatedPost.getDescription());
        assertEquals(result.getContent(), updatedPost.getContent());
    }

    @Test
    void testDeletePostSuccessfully() {
        var post = RandomGenerator.generateRandomPost();
        post.setId(RandomGenerator.generateRandomId());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        var result = postService.deletePostById(post.getId());

        assertNotNull(result);
    }
}
