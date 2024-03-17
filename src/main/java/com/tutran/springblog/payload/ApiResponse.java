package com.tutran.springblog.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse {
    @JsonProperty("data")
    Object data;

    @JsonProperty("errors")
    List<String> errors;
}
