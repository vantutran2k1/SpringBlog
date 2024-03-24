package com.tutran.springblog.api.payload.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostRequestDto {
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
