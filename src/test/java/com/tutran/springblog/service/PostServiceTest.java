package com.tutran.springblog.service;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.mapper.PostMapper;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import com.tutran.springblog.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.payload.post.PostPartialUpdateRequestDto;
import com.tutran.springblog.payload.post.PostRequestDto;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.impl.PostServiceImpl;
import com.tutran.springblog.utils.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @Test
    void testCreatePostSuccessfully() {
        PostRequestDto postRequestDto = RandomGenerator.generateRandomPostRequestDto();

        var post = this.getPostFromPostRequestDto(postRequestDto);
        var newPost = this.getPostFromPostRequestDto(postRequestDto, RandomGenerator.generateRandomId());

        when(postMapper.postRequestDtoToPost(postRequestDto)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(newPost);
        when(postMapper.postToPostResponseDto(newPost)).thenReturn(this.getPostResponseDtoFromPost(newPost));

        var result = postService.createPost(postRequestDto);

        verify(postMapper).postRequestDtoToPost(postRequestDto);
        verify(postRepository).save(post);
        verify(postMapper).postToPostResponseDto(newPost);

        assertNotNull(result);
        assertEquals(newPost.getId(), result.getId());
        assertEquals(newPost.getTitle(), result.getTitle());
        assertEquals(newPost.getDescription(), result.getDescription());
        assertEquals(newPost.getContent(), result.getContent());
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

        List<Post> mockPosts = List.of(RandomGenerator.generateRandomPost(), RandomGenerator.generateRandomPost());

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        Page<Post> mockPage = new PageImpl<>(mockPosts, pageable, mockPosts.size());

        when(postRepository.findAll(pageable)).thenReturn(mockPage);
        when(postMapper.postToPostResponseDto(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            return this.getPostResponseDtoFromPost(post);
        });

        ResponseDtoWithMeta<List<PostResponseDto>> result = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);

        verify(postRepository).findAll(pageable);
        verify(postMapper, times(mockPosts.size())).postToPostResponseDto(any());

        assertNotNull(result);
        assertEquals(mockPosts.size(), result.getData().size());
        assertEquals(mockPosts.size(), result.getMeta().getTotalElements());
        assertTrue(result.getMeta().isLast());
    }

    @Test
    void testGetPostByIdSuccessfully() {
        var post = RandomGenerator.generateRandomPostWithComments();

        var comments = post.getComments();
        var commentResponses = comments.stream().map(comment -> CommentResponseDto.builder()
                .id(comment.getId())
                .name(comment.getName())
                .email(comment.getEmail())
                .body(comment.getBody())
                .build()).collect(Collectors.toSet());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postMapper.postToPostDetailsResponseDto(post)).thenReturn(
                PostDetailsResponseDto.builder()
                        .id(post.getId())
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .content(post.getContent())
                        .comments(commentResponses)
                        .build()
        );

        var result = postService.getPostById(post.getId());

        verify(postRepository).findById(post.getId());
        verify(postMapper).postToPostDetailsResponseDto(post);

        assertNotNull(result);
        assertEquals(result.getId(), post.getId());
        assertEquals(result.getTitle(), post.getTitle());
        assertEquals(result.getDescription(), post.getDescription());
        assertEquals(result.getContent(), post.getContent());
        assertEquals(result.getComments().size(), post.getComments().size());
    }

    @Test
    void testPatchUpdateTitlePostSuccessfully() {
        var post = RandomGenerator.generateRandomPost();

        var updateRequest = PostPartialUpdateRequestDto.builder().title(RandomGenerator.generateRandomString()).build();
        var updatedPost = post.toBuilder().title(updateRequest.getTitle()).build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);
        when(postMapper.postToPostResponseDto(any(Post.class))).thenReturn(this.getPostResponseDtoFromPost(updatedPost));

        var result = postService.patchUpdatePostById(post.getId(), updateRequest);

        verify(postRepository).findById(post.getId());
        verify(postMapper).postToPostResponseDto(updatedPost);

        assertNotNull(result);
        assertEquals(result.getId(), updatedPost.getId());
        assertEquals(result.getTitle(), updatedPost.getTitle());
        assertEquals(result.getDescription(), updatedPost.getDescription());
        assertEquals(result.getContent(), updatedPost.getContent());
    }

    @Test
    void testPatchUpdateTitleDuplicateThrowException() {
        var post = RandomGenerator.generateRandomPost();

        var updateRequest = PostPartialUpdateRequestDto.builder().title(RandomGenerator.generateRandomString()).build();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.existsByTitle(updateRequest.getTitle())).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> postService.patchUpdatePostById(post.getId(), updateRequest));
    }

    @Test
    void testUpdatePostSuccessfully() {
        var post = RandomGenerator.generateRandomPost();

        var updateRequest = RandomGenerator.generateRandomPostRequestDto();
        var updatedPost = this.getPostFromPostRequestDto(updateRequest, post.getId());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);
        when(postMapper.postToPostResponseDto(any(Post.class))).thenReturn(this.getPostResponseDtoFromPost(updatedPost));

        var result = postService.updatePostById(post.getId(), updateRequest);

        verify(postRepository).findById(post.getId());
        verify(postMapper).postToPostResponseDto(updatedPost);

        assertNotNull(result);
        assertEquals(result.getId(), updatedPost.getId());
        assertEquals(result.getTitle(), updatedPost.getTitle());
        assertEquals(result.getDescription(), updatedPost.getDescription());
        assertEquals(result.getContent(), updatedPost.getContent());
    }

    @Test
    void testDeletePostSuccessfully() {
        var post = RandomGenerator.generateRandomPost();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        var result = postService.deletePostById(post.getId());

        assertNotNull(result);
    }

    private PostResponseDto getPostResponseDtoFromPost(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .content(post.getContent())
                .build();
    }

    private Post getPostFromPostRequestDto(PostRequestDto postRequestDto) {
        return Post.builder()
                .title(postRequestDto.getTitle())
                .description(postRequestDto.getDescription())
                .content(postRequestDto.getContent())
                .build();
    }

    private Post getPostFromPostRequestDto(PostRequestDto postRequestDto, long postId) {
        return getPostFromPostRequestDto(postRequestDto).toBuilder().id(postId).build();
    }
}
