package com.tutran.springblog.api.service;

import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.payload.meta.ResponseDtoWithMeta;
import com.tutran.springblog.api.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.api.payload.post.PostPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.post.PostRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;

import java.util.List;

public interface PostService {
    PostResponseDto createPost(PostRequestDto postRequestDto);

    ResponseDtoWithMeta<List<PostResponseDto>> getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDetailsResponseDto getPostById(long id);

    PostResponseDto patchUpdatePostById(long id, PostPartialUpdateRequestDto postPartialUpdateRequestDto);

    PostResponseDto updatePostById(long id, PostRequestDto postRequestDto);

    String deletePostById(long id);

    Post getPostByIdOrThrowException(long postId);
}
