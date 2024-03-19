package com.tutran.springblog.payload.post;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tutran.springblog.payload.PaginationMeta;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PostResponseDtoWithMeta {
    @JsonProperty(value = "posts")
    List<PostResponseDto> posts;

    @JsonProperty(value = "meta")
    PaginationMeta meta;
}
