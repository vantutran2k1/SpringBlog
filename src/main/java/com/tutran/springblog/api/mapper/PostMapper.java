package com.tutran.springblog.api.mapper;

import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.api.payload.post.PostRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    PostResponseDto postToPostResponseDto(Post post);

    PostDetailsResponseDto postToPostDetailsResponseDto(Post post);

    Post postRequestDtoToPost(PostRequestDto postRequestDto);
}
