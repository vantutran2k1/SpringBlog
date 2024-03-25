package com.tutran.springblog.api.controller;

import com.tutran.springblog.api.payload.ApiResponse;
import com.tutran.springblog.api.payload.comment.CommentPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.comment.CommentRequestDto;
import com.tutran.springblog.api.payload.comment.CommentResponseDto;
import com.tutran.springblog.api.service.CommentService;
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
@RequestMapping("/api")
@Tag(name = "CRUD REST APIs for Comment Resource")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @SecurityRequirement(name = "Bear Authentication")
    @PostMapping("/posts/{postId}/comments")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable(value = "postId") long postId,
            @RequestBody @Valid CommentRequestDto commentRequestDto
    ) {
        ApiResponse<CommentResponseDto> apiResponse = new ApiResponse<>(
                commentService.createComment(postId, commentRequestDto)
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentResponseDto>>> getCommentsByPostId(
            @PathVariable(value = "postId") long postId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) @Min(value = 0) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) @Min(value = 1) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        var comments = commentService.getCommentsByPostId(postId, pageNo, pageSize, sortBy, sortDir);

        ApiResponse<List<CommentResponseDto>> apiResponse = new ApiResponse<>();
        apiResponse.setData(comments.getData());
        apiResponse.setMeta(comments.getMeta());

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> getCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId
    ) {
        ApiResponse<CommentResponseDto> apiResponse = new ApiResponse<>(commentService.getCommentById(postId, commentId));
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PatchMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponseDto>> patchUpdateCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId,
            @RequestBody @Valid CommentPartialUpdateRequestDto commentPartialUpdateRequestDto
    ) {
        ApiResponse<CommentResponseDto> apiResponse = new ApiResponse<>(commentService.patchUpdateCommentById(
                postId, commentId, commentPartialUpdateRequestDto
        ));
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @PutMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId,
            @RequestBody @Valid CommentRequestDto commentRequestDto
    ) {
        ApiResponse<CommentResponseDto> apiResponse = new ApiResponse<>(commentService.updateCommentById(
                postId, commentId, commentRequestDto
        ));
        return ResponseEntity.ok(apiResponse);
    }

    @SecurityRequirement(name = "Bear Authentication")
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId
    ) {
        ApiResponse<String> apiResponse = new ApiResponse<>(commentService.deleteCommentById(postId, commentId));
        return ResponseEntity.ok(apiResponse);
    }
}
