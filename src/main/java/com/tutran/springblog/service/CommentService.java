package com.tutran.springblog.service;

import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.payload.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(long postId, CommentCreateRequest commentCreateRequest);

    List<CommentResponseDto> getCommentsByPostId(long postId);

    CommentResponseDto getCommentById(long postId, long commentId);
}
