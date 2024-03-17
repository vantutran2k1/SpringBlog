package com.tutran.springblog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostCreateRequest {
    @JsonProperty(value = "title")
    @NotEmpty(message = "title must not be empty")
    private String title;

    @JsonProperty(value = "description")
    @NotEmpty(message = "description must not be empty")
    private String description;

    @JsonProperty(value = "content")
    @NotEmpty(message = "content must not be empty")
    private String content;
}
