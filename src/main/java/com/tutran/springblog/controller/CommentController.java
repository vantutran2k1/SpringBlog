package com.tutran.springblog.controller;

import com.tutran.springblog.payload.ApiResponse;
import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.service.CommentService;
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
}
