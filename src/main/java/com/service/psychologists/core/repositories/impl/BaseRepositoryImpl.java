package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.BaseRepository;
import com.service.psychologists.core.repositories.EqualityOperator;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;

public class BaseRepositoryImpl<T> implements BaseRepository<T> {

    final private EntityManager em;

    public BaseRepositoryImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public <E extends Comparable<E>> Predicate createPredicate(Root<T> root, SearchPredicateCriteria<E> searchPredicateCriteria)
            throws IllegalArgumentException {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        EqualityOperator operator = searchPredicateCriteria.getOperator();
        String name = searchPredicateCriteria.getName();
        E value = searchPredicateCriteria.getValue();

        switch (operator) {
            case EQ:
                return cb.equal(root.get(name), value);
            case NE:
                return cb.notEqual(root.get(name), value);
            case GT:
                if (value instanceof Date) {
                    return cb.greaterThan(root.get(name), (Date) value);
                } else if (value instanceof Number) {
                    return cb.gt(root.get(name).as(Number.class), (Number) value);
                } else {
                    throw new IllegalArgumentException("GT operator can only be applied to instances of Number or Date.");
                }
            case GE:
                if (value instanceof Date) {
                    return cb.greaterThanOrEqualTo(root.get(name), (Date) value);
                } else if (value instanceof Number) {
                    return cb.ge(root.get(name).as(Number.class), (Number) value);
                } else {
                    throw new IllegalArgumentException("GE operator can only be applied to instances of Number or Date.");
                }
            case LT:
                if (value instanceof Date) {
                    return cb.lessThan(root.get(name), (Date) value);
                } else if (value instanceof Number) {
                    return cb.lt(root.get(name).as(Number.class), (Number) value);
                } else {
                    throw new IllegalArgumentException("LT operator can only be applied to instances of Number or Date.");
                }
            case LE:
                if (value instanceof Date) {
                    return cb.lessThanOrEqualTo(root.get(name), (Date) value);
                } else if (value instanceof Number) {
                    return cb.le(root.get(name).as(Number.class), (Number) value);
                } else {
                    throw new IllegalArgumentException("LE operator can only be applied to instances of Number or Date.");
                }
            default:
                throw new IllegalArgumentException("Unsupported operator: " + operator);
        }
    }


    @Override
    public Predicate createPredicate(Root<T> root, List<SearchPredicateCriteria<?>> criteriaList) {
//        List<Predicate> predicates = criteriaList.stream()
//                .map(criteria -> createPredicate(root, (List<SearchPredicateCriteria<?>>) criteria))
//                .collect(Collectors.toList());
        return null;
    }
}
