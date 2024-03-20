package com.tutran.springblog.payload.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostPartialUpdateRequest {
    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "content")
    private String content;
}
