package com.tutran.springblog.service;

import com.tutran.springblog.payload.PostCreateRequest;
import com.tutran.springblog.payload.PostCreateResponse;

public interface PostService {
    PostCreateResponse createPost(PostCreateRequest postCreateRequest);
}
