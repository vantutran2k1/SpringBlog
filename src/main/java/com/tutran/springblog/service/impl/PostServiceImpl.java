package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.mapper.PostMapper;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostUpdateRequest;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostResponseDto createPost(PostCreateRequest postCreateRequest) {
        String title = postCreateRequest.getTitle();
        this.validateNotDuplicatePostTitle(title);

        Post post = Post.builder()
                .title(title)
                .description(postCreateRequest.getDescription())
                .content(postCreateRequest.getContent())
                .build();

        Post newPost = postRepository.save(post);
        return postMapper.postToPostResponseDto(newPost);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(postMapper::postToPostResponseDto).toList();
    }

    @Override
    public PostResponseDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(this.getPostNotFoundMessage(id))
        );
        return postMapper.postToPostResponseDto(post);
    }

    @Override
    @Transactional
    public PostResponseDto updatePostById(long id, PostUpdateRequest postUpdateRequest) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(this.getPostNotFoundMessage(id))
        );

        String title = postUpdateRequest.getTitle();
        String description = postUpdateRequest.getDescription();
        String content = postUpdateRequest.getContent();


        boolean needUpdate = false;
        if (this.isNotEmpty(title)) {
            if (!title.equals(post.getTitle())) {
                this.validateNotDuplicatePostTitle(title);
            }
            post.setTitle(title);
            needUpdate = true;
        }
        if (this.isNotEmpty(description)) {
            post.setDescription(description);
            needUpdate = true;
        }
        if (this.isNotEmpty(content)) {
            post.setContent(content);
            needUpdate = true;
        }

        if (needUpdate) {
            Post updatedPost = postRepository.save(post);
            return postMapper.postToPostResponseDto(updatedPost);
        }
        return postMapper.postToPostResponseDto(post);
    }

    private boolean isNotEmpty(String property) {
        return property != null && !property.isEmpty();
    }

    private void validateNotDuplicatePostTitle(String title) {
        boolean isExisted = postRepository.existsByTitle(title);
        if (isExisted) {
            throw new DuplicateKeyException(this.getDuplicatePostTitleMessage(title));
        }
    }

    private String getPostNotFoundMessage(long id) {
        return String.format("Could not find post with id %s", id);
    }

    private String getDuplicatePostTitleMessage(String title) {
        return String.format("Post with title '%s' already exists", title);
    }
}
