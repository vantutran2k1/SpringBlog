package com.tutran.springblog.payload.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentPartialUpdateRequestDto {
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "email")
    @Email
    private String email;

    @JsonProperty(value = "body")
    private String body;
}
