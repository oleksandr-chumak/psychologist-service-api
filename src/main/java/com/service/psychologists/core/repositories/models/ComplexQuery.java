package com.service.psychologists.core.repositories.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class ComplexQuery {

    private List<SearchPredicateCriteria<?>> filter;

    private List<Order> order;

    private Pageable pageable;

    private List<String> join;
}
