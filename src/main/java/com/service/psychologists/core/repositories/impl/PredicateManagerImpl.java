package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.interfaces.PredicateManager;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PredicateManagerImpl<T> implements PredicateManager<T> {

    private final EntityManager em;

    public PredicateManagerImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public Predicate createPredicate(Path<?> path, SearchPredicateCriteria<?> searchPredicateCriteria)
            throws IllegalArgumentException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        EqualityOperator operator = searchPredicateCriteria.getOperator();
        Object value = searchPredicateCriteria.getValue();

        return switch (operator) {
            case EQ -> cb.equal(path, value);
            case NE -> cb.notEqual(path, value);
            case GT -> createGreaterThanPredicate(cb, path, value);
            case GE -> createGreaterThanOrEqualToPredicate(cb, path, value);
            case LT -> createLessThanPredicate(cb, path, value);
            case LE -> createLessThanOrEqualToPredicate(cb, path, value);
        };
    }

    @Override
    public Predicate createPredicate(List<SearchPredicateCriteria<?>> filters, Root<T> root, Map<String, Join> joinMap) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }

        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();

        filters.forEach(filter -> processFilter(filter, root, joinMap, orPredicates, andPredicates));

        return combinePredicates(orPredicates, andPredicates);
    }

    private void processFilter(SearchPredicateCriteria<?> filter, Root<T> root, Map<String, Join> joinMap,
                               List<Predicate> orPredicates, List<Predicate> andPredicates) {
        String[] splitName = filter.getName().split("\\.");
        String path = splitName.length > 1 ? String.join(".", Arrays.copyOf(splitName, splitName.length - 1)) : null;
        String field = splitName[splitName.length - 1];

        Path<?> targetPath = (path != null) ? getJoinPath(joinMap, path, filter.getName()).get(field) : root.get(field);

        if (filter.getCondition() == ConditionOperator.AND) {
            andPredicates.add(createPredicate(targetPath, filter));
        } else {
            orPredicates.add(createPredicate(targetPath, filter));
        }
    }

    private Join getJoinPath(Map<String, Join> joinMap, String path, String filterName) {
        Join joinTable = joinMap.get(path);
        if (joinTable == null) {
            throw new IllegalArgumentException("Cannot find join for value with name " + filterName);
        }
        return joinTable;
    }

    private Predicate combinePredicates(List<Predicate> orPredicates, List<Predicate> andPredicates) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        if (!orPredicates.isEmpty() && !andPredicates.isEmpty()) {
            return cb.and(resolvePredicates(cb, orPredicates, true), resolvePredicates(cb, andPredicates, false));
        } else if (!orPredicates.isEmpty()) {
            return resolvePredicates(cb, orPredicates, true);
        } else {
            return resolvePredicates(cb, andPredicates, false);
        }
    }

    private Predicate resolvePredicates(CriteriaBuilder cb, List<Predicate> predicates, boolean isOr) {
        if (predicates == null || predicates.isEmpty()) {
            throw new IllegalArgumentException("Predicate cannot be null or empty");
        }
        return predicates.size() == 1 ? predicates.get(0) : (isOr ? cb.or(predicates.toArray(new Predicate[0])) : cb.and(predicates.toArray(new Predicate[0])));
    }

    private Predicate createGreaterThanPredicate(CriteriaBuilder cb, Path<?> path, Object value) {
        if (value instanceof Date) {
            return cb.greaterThan(path.as(Date.class), (Date) value);
        } else if (value instanceof Number) {
            return cb.gt(path.as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("GT operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createGreaterThanOrEqualToPredicate(CriteriaBuilder cb, Path<?> path, Object value) {
        if (value instanceof Date) {
            return cb.greaterThanOrEqualTo(path.as(Date.class), (Date) value);
        } else if (value instanceof Number) {
            return cb.ge(path.as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("GE operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createLessThanPredicate(CriteriaBuilder cb, Path<?> path, Object value) {
        if (value instanceof Date) {
            return cb.lessThan(path.as(Date.class), (Date) value);
        } else if (value instanceof Number) {
            return cb.lt(path.as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("LT operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createLessThanOrEqualToPredicate(CriteriaBuilder cb, Path<?> path, Object value) {
        if (value instanceof Date) {
            return cb.lessThanOrEqualTo(path.as(Date.class), (Date) value);
        } else if (value instanceof Number) {
            return cb.le(path.as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("LE operator can only be applied to instances of Number or Date.");
        }
    }
}
