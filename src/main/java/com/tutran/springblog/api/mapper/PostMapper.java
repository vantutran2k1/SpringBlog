package com.tutran.springblog.api.mapper;

import com.tutran.springblog.api.entity.Category;
import com.tutran.springblog.api.entity.Post;
import com.tutran.springblog.api.payload.post.PostDetailsResponseDto;
import com.tutran.springblog.api.payload.post.PostRequestDto;
import com.tutran.springblog.api.payload.post.PostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PostMapper {
    @Mapping(source = "category", target = "categoryId", qualifiedByName = "categoryToCategoryId")
    PostResponseDto postToPostResponseDto(Post post);

    @Mapping(source = "category", target = "categoryId", qualifiedByName = "categoryToCategoryId")
    PostDetailsResponseDto postToPostDetailsResponseDto(Post post);

    Post postRequestDtoToPost(PostRequestDto postRequestDto);

    @Named("categoryToCategoryId")
    static long categoryToCategoryId(Category category) {
        return category.getId();
    }
}
