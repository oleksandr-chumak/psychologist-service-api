package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.interfaces.PredicateManager;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public Predicate createPredicate(Root<T> root, List<SearchPredicateCriteria<?>> criteriaList) {
        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        for (SearchPredicateCriteria<?> criteria : criteriaList) {
            Predicate predicate = createPredicate(root, criteria);
            if (criteria.getCondition() == ConditionOperator.OR) {
                orPredicates.add(predicate);
            } else {
                andPredicates.add(predicate);
            }
        }

        List<Predicate> finalPredicate = new ArrayList<>();

        if (!orPredicates.isEmpty()) {
            Predicate orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
            finalPredicate.add(orPredicate);
        }

        if (!andPredicates.isEmpty()) {
            Predicate andPredicate = andPredicates.size() == 1 ? andPredicates.get(0) : criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
            finalPredicate.add(andPredicate);

            return finalPredicate.size() > 1 ? criteriaBuilder.or(finalPredicate.toArray(new Predicate[0])) : andPredicate;
        }

        return finalPredicate.isEmpty() ? criteriaBuilder.conjunction() : finalPredicate.get(0);
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
