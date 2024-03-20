package com.tutran.springblog.payload.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutran.springblog.payload.comment.CommentResponseDto;
import lombok.Data;

import java.util.Set;

@Data
public class PostDetailsResponseDto {
    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "comments")
    private Set<CommentResponseDto> comments;
}
