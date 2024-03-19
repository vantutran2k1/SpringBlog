package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostUpdateRequest;
import com.tutran.springblog.service.PostService;
import com.tutran.springblog.utils.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<ApiResponse> getAllPosts(
            @RequestParam(
                    value = "pageNo",
                    defaultValue = AppConstants.DEFAULT_PAGE_NUMBER,
                    required = false
            ) @Min(value = 0) int pageNo,
            @RequestParam(
                    value = "pageSize",
                    defaultValue = AppConstants.DEFAULT_PAGE_SIZE,
                    required = false
            ) @Min(value = 1) int pageSize,
            @RequestParam(
                    value = "sortBy",
                    defaultValue = AppConstants.DEFAULT_SORT_BY,
                    required = false
            ) String sortBy,
            @RequestParam(
                    value = "sortDir",
                    defaultValue = AppConstants.DEFAULT_SORT_DIRECTION,
                    required = false
            ) String sortDir
    ) {
        var posts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.builder().data(posts.getPosts()).meta(posts.getMeta()).build());
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
