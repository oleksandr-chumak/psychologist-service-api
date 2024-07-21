package com.service.psychologists.core.repositories;

import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchPredicateCriteria<T> {

    private String name;

    private T value;

    private EqualityOperator operator;

    private ConditionOperator condition;
}
