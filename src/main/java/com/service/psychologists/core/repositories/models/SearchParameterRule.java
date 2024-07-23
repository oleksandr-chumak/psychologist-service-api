package com.service.psychologists.core.repositories.models;

import com.service.psychologists.core.repositories.enums.EqualityOperator;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchParameterRule {
    private final String name;
    private final EqualityOperator[] allowedOperators;
    private final Class<?> type;

    public boolean isValidOperator(EqualityOperator operator) {
        for (EqualityOperator allowedOperator : allowedOperators) {
            if (allowedOperator == operator) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidValue(Object value) {
        return type.isInstance(value);
    }
}
