package com.tutran.springblog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaginationMeta {
    @JsonProperty(value = "page_number")
    int pageNo;

    @JsonProperty(value = "page_size")
    int pageSize;

    @JsonProperty(value = "total_elements")
    long totalElements;

    @JsonProperty(value = "total_pages")
    int totalPages;

    @JsonProperty(value = "is_last")
    boolean isLast;
}
