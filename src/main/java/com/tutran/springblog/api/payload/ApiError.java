package com.tutran.springblog.api.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiError {
    @JsonProperty(value = "message")
    String message;

    @JsonProperty(value = "error")
    Object error;

    @JsonProperty(value = "status")
    HttpStatus status;
}
