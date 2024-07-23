package com.service.psychologists.core.repositories.models;

public class SearchParameterValidator {

    private final SearchParameterRule[] searchParameterRules;

    public SearchParameterValidator(SearchParameterRule[] searchParameterRules) {
        this.searchParameterRules = searchParameterRules;
    }

    public void validate(SearchPredicateCriteria<?> searchPredicateCriteria) throws IllegalArgumentException {
        SearchParameterRule foundSearchParameterRule = null;

        for (SearchParameterRule rule : searchParameterRules) {
            if (rule.getName().equals(searchPredicateCriteria.getName())) {
                foundSearchParameterRule = rule;
                break;
            }
        }

        if (foundSearchParameterRule == null) {
            throw new IllegalArgumentException("Condition with name " + searchPredicateCriteria.getName() + " is not allowed");
        }

        boolean isOperatorValid = foundSearchParameterRule.isValidOperator(searchPredicateCriteria.getOperator());

        if (!isOperatorValid) {
            throw new IllegalArgumentException("Condition with name " + searchPredicateCriteria.getName() + " doesn't support " + searchPredicateCriteria.getOperator() + " operator");
        }

        boolean isValueValid = foundSearchParameterRule.isValidValue(searchPredicateCriteria.getValue());

        if (!isValueValid) {
            throw new IllegalArgumentException("Value for condition with name " + searchPredicateCriteria.getName() + " must be typeof " + foundSearchParameterRule.getType().getSimpleName());
        }

    }

}
