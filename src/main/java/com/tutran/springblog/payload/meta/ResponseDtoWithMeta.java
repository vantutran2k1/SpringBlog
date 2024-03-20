package com.tutran.springblog.payload.meta;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ResponseDtoWithMeta<T> {
    @JsonProperty(value = "data")
    T data;

    @JsonProperty(value = "meta")
    PaginationMeta meta;
}
