package com.tutran.springblog.service;

import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostCreateRequest postCreateRequest);

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPostById(long id);
}
