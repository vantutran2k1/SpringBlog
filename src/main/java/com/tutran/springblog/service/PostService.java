package com.tutran.springblog.service;

import com.tutran.springblog.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostUpdateRequest;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostCreateRequest postCreateRequest);

    ResponseDtoWithMeta<List<PostResponseDto>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostResponseDto getPostById(long id);

    PostResponseDto updatePostById(long id, PostUpdateRequest postUpdateRequest);

    String deletePostById(long id);
}
