package com.tutran.springblog.mapper;

import com.tutran.springblog.entity.Post;
import com.tutran.springblog.payload.PostCreateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    PostCreateResponse postToPostCreateResponse(Post post);
}
