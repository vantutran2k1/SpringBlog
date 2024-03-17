package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostUpdateRequest;
import com.tutran.springblog.service.PostService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ApiResponse> createPost(@RequestBody @Valid PostCreateRequest postCreateRequest) {
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

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePostById(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid PostUpdateRequest postUpdateRequest
    ) {
        return ResponseEntity.ok(ApiResponse.builder().data(postService.updatePostById(id, postUpdateRequest)).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePostById(@PathVariable(name = "id") long id) {
        postService.deletePostById(id);

        return ResponseEntity.ok(ApiResponse.builder().data("Post entity deleted successfully").build());
    }
}
