package com.service.psychologists.core.mappers;

import com.service.psychologists.core.models.PaginationResponse;
import com.service.psychologists.core.utils.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PageToPaginationResponseMapper<T> implements Mapper<Page<T>, PaginationResponse<T>> {
    @Override
    public PaginationResponse<T> mapTo(Page<T> page) {
        PaginationResponse<T> paginationResponse = new PaginationResponse<>();
        paginationResponse.setPage(page.getPageable().getPageNumber());
        paginationResponse.setItems(page.getContent());
        paginationResponse.setTotalItems(page.getTotalElements());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPageSize(page.getNumberOfElements());
        return paginationResponse;
    }

    @Override
    public Page<T> mapFrom(PaginationResponse<T> paginationResponse) {
        return null;
    }
}
