package com.tutran.springblog.api.payload.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryResponseDto {
    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;
}
