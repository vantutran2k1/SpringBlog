package com.tutran.springblog.service;

import com.tutran.springblog.payload.PostCreateRequest;
import com.tutran.springblog.payload.PostResponseDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostCreateRequest postCreateRequest);

    List<PostResponseDto> getAllPosts();
}
