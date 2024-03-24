package com.tutran.springblog.api.payload.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @JsonProperty(value = "name")
    @NotEmpty(message = "must not be empty")
    private String name;

    @JsonProperty(value = "description")
    @NotEmpty(message = "must not be empty")
    private String description;
}
