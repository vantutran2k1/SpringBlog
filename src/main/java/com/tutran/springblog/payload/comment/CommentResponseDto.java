package com.tutran.springblog.payload.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentResponseDto {
    @JsonProperty(value = "id")
    private long id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "body")
    private String body;
}
