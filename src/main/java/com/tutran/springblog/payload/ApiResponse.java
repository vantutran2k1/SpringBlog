package com.tutran.springblog.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @JsonProperty(value = "data")
    Object data;

    @JsonProperty(value = "meta")
    Object meta;

    @JsonProperty(value = "status_code")
    HttpStatus statusCode;
}
