package com.tutran.springblog.api.controller;

import com.tutran.springblog.api.payload.ApiResponse;
import com.tutran.springblog.api.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.api.payload.post.PostPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.post.PostRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;
import com.tutran.springblog.api.service.PostService;
import com.tutran.springblog.api.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "CRUD REST APIs for Post Resource")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@RequestBody @Valid PostRequestDto postRequestDto) {
        ApiResponse<PostResponseDto> apiResponse = new ApiResponse<>(postService.createPost(postRequestDto));
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponseDto>>> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) @Min(value = 0) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) @Min(value = 1) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        var posts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir, categoryId);

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

    @SecurityRequirement(name = "Bear Authentication")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponseDto>> patchUpdatePostById(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid PostPartialUpdateRequestDto postPartialUpdateRequestDto
    ) {
        ApiResponse<PostResponseDto> apiResponse = new ApiResponse<>(postService.patchUpdatePostById(id, postPartialUpdateRequestDto));
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePostById(
            @PathVariable(name = "id") long id,
            @RequestBody @Valid PostRequestDto postRequestDto
    ) {
        ApiResponse<PostResponseDto> apiResponse = new ApiResponse<>(postService.updatePostById(id, postRequestDto));
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePostById(@PathVariable(name = "id") long id) {
        ApiResponse<String> apiResponse = new ApiResponse<>(postService.deletePostById(id));
        return ResponseEntity.ok(apiResponse);
    }
}
