package com.service.psychologists.core.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationResponse<T> {

    private int page;

    private int pageSize;

    private int totalPages;

    private long totalItems;

    private List<T> items;
}
