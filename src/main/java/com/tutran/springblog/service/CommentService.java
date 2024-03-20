package com.tutran.springblog.service;

import com.tutran.springblog.payload.comment.CommentRequestDto;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import com.tutran.springblog.payload.meta.ResponseDtoWithMeta;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(long postId, CommentRequestDto commentRequestDto);

    ResponseDtoWithMeta<List<CommentResponseDto>> getCommentsByPostId(long postId, int pageNo, int pageSize, String sortBy, String sortDir);

    CommentResponseDto getCommentById(long postId, long commentId);

    CommentResponseDto updateComment(long postId, long commentId, CommentRequestDto commentRequestDto);
}
