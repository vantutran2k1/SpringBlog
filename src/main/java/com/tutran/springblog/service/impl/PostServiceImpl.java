package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.mapper.PostMapper;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .description(postCreateRequest.getDescription())
                .content(postCreateRequest.getContent())
                .build();

        // TODO:
        //  Check for duplicate post title
        //  Return appropriate status code based on request payload
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
                () -> new EntityNotFoundException("Could not find post with id: " + id)
        );
        return postMapper.postToPostResponseDto(post);
    }
}
