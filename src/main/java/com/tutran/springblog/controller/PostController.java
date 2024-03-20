package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.payload.post.PostResponseDto;
import com.tutran.springblog.payload.post.PostUpdateRequest;
import com.tutran.springblog.service.PostService;
import com.tutran.springblog.utils.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@RequestBody @Valid PostCreateRequest postCreateRequest) {
        ApiResponse<PostResponseDto> apiResponse = new ApiResponse<>(postService.createPost(postCreateRequest));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) @Min(value = 0) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) @Min(value = 1) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        var posts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);

        ApiResponse<List<PostResponseDto>> apiResponse = new ApiResponse<>();
        apiResponse.setData(posts.getData());
        apiResponse.setMeta(posts.getMeta());

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailsResponseDto>> getPostById(@PathVariable(name = "id") long id) {
        ApiResponse<PostDetailsResponseDto> apiResponse = new ApiResponse<>(postService.getPostById(id));
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePostById(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid PostUpdateRequest postUpdateRequest
    ) {
        ApiResponse<PostResponseDto> apiResponse = new ApiResponse<>(postService.updatePostById(id, postUpdateRequest));
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePostById(@PathVariable(name = "id") long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>(postService.deletePostById(id));
        return ResponseEntity.ok(apiResponse);
    }
}
