package com.tutran.springblog.api.mapper;

import com.tutran.springblog.api.entity.Comment;
import com.tutran.springblog.api.payload.comment.CommentRequestDto;
import com.tutran.springblog.api.payload.comment.CommentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {
    CommentResponseDto commentToCommentResponseDto(Comment comment);

    Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto);
}
