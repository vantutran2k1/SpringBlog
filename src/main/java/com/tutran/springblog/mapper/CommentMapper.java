package com.tutran.springblog.mapper;

import com.tutran.springblog.entity.Comment;
import com.tutran.springblog.payload.comment.CommentCreateRequest;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentResponseDto commentToCommentResponseDto(Comment comment);

    Comment commentCreateRequestToComment(CommentCreateRequest commentCreateRequest);
}
