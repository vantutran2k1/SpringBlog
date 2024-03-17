package com.tutran.springblog.service;

import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostUpdateRequest;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostCreateRequest postCreateRequest);

    List<PostResponseDto> getAllPosts();

    PostResponseDto getPostById(long id);

    PostResponseDto updatePostById(long id, PostUpdateRequest postUpdateRequest);

    void deletePostById(long id);
}
