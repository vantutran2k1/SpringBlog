package com.tutran.springblog.service;

import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.payload.comment.CommentResponseDto;

public interface CommentService {
    CommentResponseDto createComment(long postId, CommentCreateRequest commentCreateRequest);
}
