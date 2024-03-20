package com.tutran.springblog.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @JsonProperty(value = "data")
    @NonNull
    T data;

    @JsonProperty(value = "meta")
    Object meta;

    @JsonProperty(value = "status_code")
    HttpStatus statusCode;
}
