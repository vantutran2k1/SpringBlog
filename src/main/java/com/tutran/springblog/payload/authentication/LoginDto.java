package com.tutran.springblog.payload.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @JsonProperty(value = "username_or_email")
    @NotEmpty(message = "must not be empty")
    private String usernameOrEmail;

    @JsonProperty(value = "password")
    @NotEmpty(message = "must not be empty")
    private String password;
}
