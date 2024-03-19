package com.tutran.springblog.mapper;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.payload.post.PostCreateRequest;
import com.tutran.springblog.payload.post.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    PostResponseDto postToPostResponseDto(Post post);

    Post postCreateRequestToPost(PostCreateRequest postCreateRequest);
}
