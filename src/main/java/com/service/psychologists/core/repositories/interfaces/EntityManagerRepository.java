package com.service.psychologists.core.repositories.interfaces;

import com.service.psychologists.core.repositories.models.ComplexQuery;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface EntityManagerRepository<T> {

    List<T> find(ComplexQuery complexQuery);

    Page<T> findPageable(ComplexQuery complexQuery);

    Optional<T> findOne(ComplexQuery query);

    T create(T entity);
}
