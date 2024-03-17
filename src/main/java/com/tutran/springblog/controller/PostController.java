package com.tutran.springblog.controller;

import com.tutran.springblog.payload.PostCreateRequest;
import com.tutran.springblog.payload.PostCreateResponse;
import com.tutran.springblog.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostCreateResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
        return new ResponseEntity<>(postService.createPost(postCreateRequest), HttpStatus.CREATED);
    }
}
