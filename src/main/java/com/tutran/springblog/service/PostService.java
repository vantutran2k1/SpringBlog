package com.tutran.springblog.service;

import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostResponseDtoWithMeta;
import com.tutran.springblog.payload.post.PostUpdateRequest;

public interface PostService {
    PostResponseDto createPost(PostCreateRequest postCreateRequest);

    PostResponseDtoWithMeta getAllPosts(int pageNo, int pageSize);

    PostResponseDto getPostById(long id);

    PostResponseDto updatePostById(long id, PostUpdateRequest postUpdateRequest);

    void deletePostById(long id);
}
