package com.tutran.springblog.payload.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDto {
    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "content")
    private String content;
}
