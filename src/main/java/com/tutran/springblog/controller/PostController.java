package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse> createPost(@RequestBody PostCreateRequest postCreateRequest) {
        return new ResponseEntity<>(
                ApiResponse.builder().data(postService.createPost(postCreateRequest)).build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.builder().data(postService.getAllPosts()).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(ApiResponse.builder().data(postService.getPostById(id)).build());
    }
}
