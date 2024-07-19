package com.service.psychologists.core.repositories;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public interface BaseRepository<T> {

    <E extends Comparable<E>> Predicate createPredicate(Root<T> root, SearchPredicateCriteria<E> searchPredicateCriteria) throws IllegalArgumentException;

    Predicate createPredicate(Root<T> root, List<SearchPredicateCriteria<?>> searchPredicateCriteria);
}
