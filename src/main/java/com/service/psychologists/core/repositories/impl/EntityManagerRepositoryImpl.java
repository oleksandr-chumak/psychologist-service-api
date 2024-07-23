package com.service.psychologists.core.repositories.impl;

import com.service.psychologists.core.repositories.interfaces.EntityManagerRepository;
import com.service.psychologists.core.repositories.models.ComplexQuery;
import com.service.psychologists.core.repositories.models.Order;
import com.service.psychologists.core.repositories.interfaces.PredicateManager;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import com.service.psychologists.core.repositories.enums.OrderDirection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

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
    public List<T> find(ComplexQuery complexQuery) {
        TypedQuery<T> typedQuery = createTypedQueryFromComplexQuery(complexQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Page<T> findPageable(ComplexQuery complexQuery) {
        if (complexQuery.getPageable() == null) {
            throw new IllegalArgumentException("Pageable cannot be null");
        }

        TypedQuery<T> typedQuery = createTypedQueryFromComplexQuery(complexQuery);
        long totalItemsCount = getTotalItemsCount(complexQuery.getFilter(), complexQuery.getJoin());

        List<T> results = typedQuery.getResultList();
        return new PageImpl<>(results, complexQuery.getPageable(), totalItemsCount);
    }

    @Override
    public Optional<T> findOne(ComplexQuery query) {
        try {
            return Optional.of(createTypedQueryFromComplexQuery(ComplexQuery
                    .builder()
                    .filter(query.getFilter())
                    .join(query.getJoin())
                    .build()
            ).getSingleResult());
        } catch (Exception e) {
            log.warning("Error fetching single result: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public T create(T entity) {
        try {
            entityManager.persist(entity);
            return entity;
        } catch (Exception e) {
            log.warning("Error creating entity: " + e.getMessage());
            throw new RuntimeException("Failed to create entity", e);
        }
    }

    private TypedQuery<T> createTypedQueryFromComplexQuery(ComplexQuery complexQuery) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<T> root = criteriaQuery.from(entityClass);

        Map<String, Join> joinMap = buildJoinMap(complexQuery.getJoin(), root);
        List<Predicate> predicates = buildPredicates(complexQuery.getFilter(), root, joinMap);
        List<jakarta.persistence.criteria.Order> orderList = buildOrderList(root, complexQuery.getOrder());

        if (!predicates.isEmpty()) {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
        }

        if (!orderList.isEmpty()) {
            criteriaQuery.orderBy(orderList);
        }

        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

        Pageable pageable = complexQuery.getPageable();
        if (pageable != null) {
            typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize());
        }

        return typedQuery;
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

    private long getTotalItemsCount(List<SearchPredicateCriteria<?>> filters, List<String> joins) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
            Root<T> root = criteriaQuery.from(entityClass);

            List<Predicate> predicates = buildPredicates(filters, root, buildJoinMap(joins, root));

            if (predicates.isEmpty()) {
                criteriaQuery.select(criteriaBuilder.count(root));
            } else {
                criteriaQuery.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[0]));
            }

            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (Exception e) {
            log.warning("Error fetching total count: " + e.getMessage());
            return 0;
        }
    }

    private Map<String, Join> buildJoinMap(List<String> joins, Root<T> root) {
        Map<String, Join> joinMap = new HashMap<>();

        if (joins != null) {
            joins.stream().sorted(Comparator.comparingInt(EntityManagerRepositoryImpl::countDots)).forEach(joinPath -> {
                String[] splitPath = joinPath.split("\\.");
                if (splitPath.length > 1) {
                    String pathToJoin = String.join(".", Arrays.copyOf(splitPath, splitPath.length - 1));
                    Join foundJoin = joinMap.computeIfAbsent(pathToJoin, key -> {
                        throw new IllegalArgumentException("Cannot join table. Path not found: " + key);
                    });
                    joinMap.put(joinPath, foundJoin.join(splitPath[splitPath.length - 1]));
                } else {
                    joinMap.put(joinPath, root.join(joinPath));
                }
            });
        }
        return joinMap;
    }

    private List<Predicate> buildPredicates(List<SearchPredicateCriteria<?>> filters, Root<T> root, Map<String, Join> joinMap) {
        List<Predicate> predicates = new ArrayList<>();
        if (filters != null) {
            filters.forEach(filter -> {
                String[] splitName = filter.getName().split("\\.");
                String path = splitName.length > 1 ? String.join(".", Arrays.copyOf(splitName, splitName.length - 1)) : null;
                String field = splitName[splitName.length - 1];

                if (path != null) {
                    Join joinTable = joinMap.get(path);
                    if (joinTable == null) {
                        throw new IllegalArgumentException("Cannot find join for value with name " + filter.getName());
                    }
                    predicates.add(predicateManager.createPredicate(joinTable.get(field), filter));
                } else {
                    predicates.add(predicateManager.createPredicate(root.get(field), filter));
                }
            });
        }
        return predicates;
    }

    private static int countDots(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == '.') {
                count++;
            }
        }
        return count;
    }
}
