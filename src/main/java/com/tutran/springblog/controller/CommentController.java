package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.service.CommentService;
import com.tutran.springblog.utils.AppConstants;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> createComment(
            @PathVariable(value = "postId") long postId,
            @RequestBody CommentCreateRequest commentCreateRequest
    ) {
        return new ResponseEntity<>(
                ApiResponse.builder().data(commentService.createComment(postId, commentCreateRequest)).build(),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse> getCommentsByPostId(
            @PathVariable(value = "postId") long postId,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) @Min(value = 0) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) @Min(value = 1) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        var comments = commentService.getCommentsByPostId(postId, pageNo, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.builder().data(comments.getData()).meta(comments.getMeta()).build());
    }

    @GetMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse> getCommentById(
            @PathVariable(value = "postId") long postId,
            @PathVariable(value = "commentId") long commentId
    ) {
        return ResponseEntity.ok(ApiResponse.builder().data(commentService.getCommentById(postId, commentId)).build());
    }
}
