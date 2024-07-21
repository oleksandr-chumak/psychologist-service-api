package com.service.psychologists.core.repositories;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public interface PredicateManager<T> {

    Predicate createPredicate(Root<T> root, SearchPredicateCriteria<?> searchPredicateCriteria) throws IllegalArgumentException;

    Predicate createPredicate(Root<T> root, List<SearchPredicateCriteria<?>> searchPredicateCriteria);

}
