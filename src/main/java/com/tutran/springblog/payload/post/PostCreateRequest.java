package com.tutran.springblog.payload.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostCreateRequest {
    @JsonProperty(value = "title")
    @NotEmpty(message = "must not be empty")
    private String title;

    @JsonProperty(value = "description")
    @NotEmpty(message = "must not be empty")
    private String description;

    @JsonProperty(value = "content")
    @NotEmpty(message = "must not be empty")
    private String content;
}
