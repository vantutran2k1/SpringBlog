package com.tutran.springblog.service.impl;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.mapper.PostMapper;
import com.tutran.springblog.payload.PostCreateRequest;
import com.tutran.springblog.payload.PostCreateResponse;
import com.tutran.springblog.repository.PostRepository;
import com.tutran.springblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Override
    @Transactional
    public PostCreateResponse createPost(PostCreateRequest postCreateRequest) {
        Post post = Post.builder()
                .title(postCreateRequest.getTitle())
                .description(postCreateRequest.getDescription())
                .content(postCreateRequest.getContent())
                .build();

        // TODO:
        //  Check for duplicate post title
        //  Return appropriate status code based on request payload
        Post newPost = postRepository.save(post);
        return postMapper.postToPostCreateResponse(newPost);
    }
}
