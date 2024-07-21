package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.PredicateManager;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
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
    public Predicate createPredicate(Root<T> root, SearchPredicateCriteria<?> searchPredicateCriteria)
            throws IllegalArgumentException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        EqualityOperator operator = searchPredicateCriteria.getOperator();
        String name = searchPredicateCriteria.getName();
        Object value = searchPredicateCriteria.getValue();

        return switch (operator) {
            case EQ -> cb.equal(root.get(name), value);
            case NE -> cb.notEqual(root.get(name), value);
            case GT -> createGreaterThanPredicate(cb, root, name, value);
            case GE -> createGreaterThanOrEqualToPredicate(cb, root, name, value);
            case LT -> createLessThanPredicate(cb, root, name, value);
            case LE -> createLessThanOrEqualToPredicate(cb, root, name, value);
            default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
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

    private Predicate createGreaterThanPredicate(CriteriaBuilder cb, Root<T> root, String name, Object value) {
        if (value instanceof Date) {
            return cb.greaterThan(root.get(name), (Date) value);
        } else if (value instanceof Number) {
            return cb.gt(root.get(name).as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("GT operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createGreaterThanOrEqualToPredicate(CriteriaBuilder cb, Root<T> root, String name, Object value) {
        if (value instanceof Date) {
            return cb.greaterThanOrEqualTo(root.get(name), (Date) value);
        } else if (value instanceof Number) {
            return cb.ge(root.get(name).as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("GE operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createLessThanPredicate(CriteriaBuilder cb, Root<T> root, String name, Object value) {
        if (value instanceof Date) {
            return cb.lessThan(root.get(name), (Date) value);
        } else if (value instanceof Number) {
            return cb.lt(root.get(name).as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("LT operator can only be applied to instances of Number or Date.");
        }
    }

    private Predicate createLessThanOrEqualToPredicate(CriteriaBuilder cb, Root<T> root, String name, Object value) {
        if (value instanceof Date) {
            return cb.lessThanOrEqualTo(root.get(name), (Date) value);
        } else if (value instanceof Number) {
            return cb.le(root.get(name).as(Number.class), (Number) value);
        } else {
            throw new IllegalArgumentException("LE operator can only be applied to instances of Number or Date.");
        }
    }
}
