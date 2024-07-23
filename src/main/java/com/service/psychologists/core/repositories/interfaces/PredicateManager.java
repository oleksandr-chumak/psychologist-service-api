package com.service.psychologists.core.repositories.interfaces;

import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;

public interface PredicateManager<T> {

    Predicate createPredicate(Path<?> path, SearchPredicateCriteria<?> searchPredicateCriteria) throws IllegalArgumentException;

    Predicate createPredicate(Root<T> root, List<SearchPredicateCriteria<?>> searchPredicateCriteria);

}
