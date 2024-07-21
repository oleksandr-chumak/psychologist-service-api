package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.EntityManagerRepository;
import com.service.psychologists.core.repositories.Order;
import com.service.psychologists.core.repositories.PredicateManager;
import com.service.psychologists.core.repositories.SearchPredicateCriteria;
import com.service.psychologists.core.repositories.enums.OrderDirection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log
public class EntityManagerRepositoryImpl<T> implements EntityManagerRepository<T> {

    private final EntityManager entityManager;
    private final Class<T> entityClass;
    private final PredicateManager<T> predicateManager;

    public EntityManagerRepositoryImpl(
            final EntityManager entityManager,
            final Class<T> entityClass,
            final PredicateManager<T> predicateManager
    ) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
        this.predicateManager = predicateManager;
    }

    @Override
    public List<T> find() {
        return findAll(null, null);
    }

    @Override
    public List<T> findByCriteria(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList) {
        return findAll(searchPredicateCriteriaList, null);
    }

    @Override
    public List<T> findByOrder(List<Order> orders) {
        return findAll(null, orders);
    }

    @Override
    public List<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        return findAll(searchPredicateCriteriaList, orders);
    }

    @Override
    public Page<T> find(Pageable pageable) {
        return findAll(pageable, null, null);
    }

    @Override
    public Page<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, Pageable pageable) {
        return findAll(pageable, searchPredicateCriteriaList, null);
    }

    @Override
    public Page<T> find(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders, Pageable pageable) {
        return findAll(pageable, searchPredicateCriteriaList, orders);
    }

    @Override
    public Optional<T> findOne(Predicate predicate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root).where(predicate);

        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warning("Error fetching single result: " + e.getMessage());
            return Optional.empty();
        }
    }

    private List<T> findAll(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        return createTypedQuery(null, searchPredicateCriteriaList, orders).getResultList();
    }

    private Page<T> findAll(Pageable pageable, List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        if (pageable == null) {
            throw new IllegalArgumentException("pageable cannot be null");
        }

        TypedQuery<T> typedQuery = createTypedQuery(pageable, searchPredicateCriteriaList, orders);
        long totalItemsCount = getTotalItemsCount(searchPredicateCriteriaList);

        List<T> results = typedQuery.getResultList();
        return new PageImpl<>(results, pageable, totalItemsCount);
    }

    private TypedQuery<T> createTypedQuery(Pageable pageable, List<SearchPredicateCriteria<?>> searchPredicateCriteriaList, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        Predicate finalPredicate = buildPredicate(root, searchPredicateCriteriaList);
        criteriaQuery.where(finalPredicate);

        if (orders != null && !orders.isEmpty()) {
            criteriaQuery.orderBy(buildOrderList(root, orders));
        }

        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
        if (pageable != null) {
            query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }

        return query;
    }

    private Predicate buildPredicate(Root<T> root, List<SearchPredicateCriteria<?>> searchPredicateCriteriaList) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        if (searchPredicateCriteriaList != null && !searchPredicateCriteriaList.isEmpty()) {
            return predicateManager.createPredicate(root, searchPredicateCriteriaList);
        }
        return criteriaBuilder.conjunction();
    }

    private List<jakarta.persistence.criteria.Order> buildOrderList(Root<T> root, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<jakarta.persistence.criteria.Order> orderList = new ArrayList<>();
        for (Order order : orders) {
            orderList.add(order.getDirection() == OrderDirection.ASC ?
                    criteriaBuilder.asc(root.get(order.getName())) :
                    criteriaBuilder.desc(root.get(order.getName())));
        }
        return orderList;
    }

    private long getTotalItemsCount(List<SearchPredicateCriteria<?>> searchPredicateCriteriaList) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<T> root = criteriaQuery.from(entityClass);

            Predicate finalPredicate = buildPredicate(root, searchPredicateCriteriaList);
            criteriaQuery.select(criteriaBuilder.count(root)).where(finalPredicate);

            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            log.warning("Error fetching total count: " + e.getMessage());
            return 0;
        }
    }
}
