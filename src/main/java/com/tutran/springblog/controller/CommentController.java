package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.comment.CommentRequestDto;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import com.tutran.springblog.service.CommentService;
import com.tutran.springblog.utils.AppConstants;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @PathVariable(value = "postId") long postId,
            @RequestBody CommentRequestDto commentRequestDto
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

    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        ApiResponse<CommentResponseDto> apiResponse = new ApiResponse<>(commentService.updateComment(
                postId, commentId, commentRequestDto
        ));
        return ResponseEntity.ok(apiResponse);
    }
}
