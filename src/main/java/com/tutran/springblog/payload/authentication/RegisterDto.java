package com.tutran.springblog.payload.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterDto {
    @JsonProperty(value = "name")
    @NotEmpty
    private String name;

    @JsonProperty(value = "username")
    @NotEmpty
    private String username;

    @JsonProperty(value = "email")
    @NotEmpty
    private String email;

    @JsonProperty(value = "password")
    @NotEmpty
    private String password;
}
