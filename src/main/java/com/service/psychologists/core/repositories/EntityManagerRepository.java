package com.service.psychologists.core.repositories;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EntityManagerRepository<T> {

    List<T> find();

    List<T> findByCriteria(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList);

    List<T> findByOrder(List<Order> orders);

    Page<T> find(Pageable pageable);

    List<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders);

    Page<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, Pageable pageable);

    Page<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders, Pageable pageable);

    Optional<T> findOne(Predicate predicate);
}
