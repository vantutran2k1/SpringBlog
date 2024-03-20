package com.tutran.springblog.payload.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class CommentPartialUpdateRequestDto {
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "email")
    @Email
    private String email;

    @JsonProperty(value = "body")
    private String body;
}
