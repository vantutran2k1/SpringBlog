package com.tutran.springblog.api.service;

import com.tutran.springblog.api.payload.comment.CommentPartialUpdateRequestDto;
import com.tutran.springblog.api.payload.comment.CommentRequestDto;
import com.tutran.springblog.api.payload.comment.CommentResponseDto;
import com.tutran.springblog.api.payload.meta.ResponseDtoWithMeta;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(long postId, CommentRequestDto commentRequestDto);

    ResponseDtoWithMeta<List<CommentResponseDto>> getCommentsByPostId(long postId, int pageNo, int pageSize, String sortBy, String sortDir);

    CommentResponseDto getCommentById(long postId, long commentId);

    CommentResponseDto patchUpdateCommentById(long postId, long commentId, CommentPartialUpdateRequestDto commentPartialUpdateRequestDto);

    CommentResponseDto updateCommentById(long postId, long commentId, CommentRequestDto commentRequestDto);

    String deleteCommentById(long postId, long commentId);
}
