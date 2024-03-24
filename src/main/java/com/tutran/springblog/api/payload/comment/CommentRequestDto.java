package com.tutran.springblog.api.payload.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentRequestDto {
    @JsonProperty(value = "name")
    @NotEmpty(message = "must not be empty")
    private String name;

    @JsonProperty(value = "email")
    @NotEmpty(message = "must not be empty")
    @Email
    private String email;

    @JsonProperty(value = "body")
    @NotEmpty(message = "must not be empty")
    private String body;
}
