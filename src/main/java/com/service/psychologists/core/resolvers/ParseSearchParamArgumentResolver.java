package com.service.psychologists.core.resolvers;

import com.service.psychologists.core.annotations.ParsedSearchRequestParam;
import com.service.psychologists.core.helpers.ValueParser;
import com.service.psychologists.core.repositories.enums.ConditionOperator;
import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.models.SearchParameterValidator;
import com.service.psychologists.core.repositories.models.SearchPredicateCriteria;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ParseSearchParamArgumentResolver implements HandlerMethodArgumentResolver {

    private final char doubleQuote = '\"';
    private final char singleQuote = '\'';

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ParsedSearchRequestParam.class);
    }

    @Override
    public List<SearchPredicateCriteria<Object>> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        ParsedSearchRequestParam annotation = parameter.getParameterAnnotation(ParsedSearchRequestParam.class);

        if (annotation == null) {
            return null;
        }

        String paramName = annotation.name();
        SearchParameterValidator validator = annotation.validator().getDeclaredConstructor().newInstance();
        String paramValue = webRequest.getParameter(paramName);

        if (paramValue == null) {
            return new ArrayList<>();
        }

        List<String>[] parsingResult = parseRequestParamString(paramValue);
        List<String> conditionOperators = parsingResult[0];
        List<String> conditions = parsingResult[1];

        List<SearchPredicateCriteria<Object>> searchPredicateCriteria = new ArrayList<>();

        for (int i = 0; i < conditions.size(); i++) {
            String condition = conditions.get(i);
            String conditionOperator = conditionOperators.isEmpty() ? "and" : conditionOperators.get(i == 0 ? 0 : i - 1);
            try {
                searchPredicateCriteria.add(transformConditionStringToSearchPredicateCriteria(condition.trim(), ConditionOperator.valueOf(conditionOperator.toUpperCase())));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condition " + (i + 1) + ": " + e.getMessage());
            }
        }

        for (SearchPredicateCriteria<Object> criteria : searchPredicateCriteria) {
            try {
                validator.validate(criteria);
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
            }
        }

        return searchPredicateCriteria;
    }

    private List<String>[] parseRequestParamString(String requestParam) {
        String[] splitString = requestParam.split(" ");
        List<String> conditionOperators = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        StringBuilder acc = new StringBuilder();

        for (String s : splitString) {
            if (s.equals("and") || s.equals("or")) {
                conditionOperators.add(s);
                conditions.add(acc.toString());
                acc = new StringBuilder();
            } else {
                acc.append(" ").append(s);
            }
        }

        if (!acc.isEmpty() || conditionOperators.size() - 1 != conditions.size()) {
            conditions.add(acc.toString().isEmpty() ? " " : acc.toString());
        }

        return new List[]{conditionOperators, conditions};
    }

    private SearchPredicateCriteria<Object> transformConditionStringToSearchPredicateCriteria(String condition, ConditionOperator conditionOperator) throws IllegalArgumentException {
        String[] splitConditions = condition.split(" ");
        String name = splitConditions.length > 0 ? splitConditions[0] : null;

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        String stringOperator = splitConditions.length > 1 ? splitConditions[1] : null;

        if (stringOperator == null || stringOperator.isEmpty()) {
            throw new IllegalArgumentException("Operator cannot be empty");
        }

        EqualityOperator operator;

        try {
            operator = EqualityOperator.valueOf(stringOperator.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Operator '" + stringOperator + "' is not a valid operator");
        }

        if (splitConditions.length == 2) {
            throw new IllegalArgumentException("Value cannot be empty");
        }

        String stringValue = String.join(" ", Arrays.copyOfRange(splitConditions, 2, splitConditions.length));
        char firstChar = stringValue.charAt(0);
        char lastChar = stringValue.charAt(stringValue.length() - 1);
        boolean isFirstCharQuote = firstChar == doubleQuote || firstChar == singleQuote;
        boolean isLastCharQuote = lastChar == doubleQuote || firstChar == singleQuote;
        Object value;

        if (splitConditions.length == 3) {
            String stringValueWithoutQuote = stringValue.substring(isFirstCharQuote ? 1 : 0, stringValue.length() - (isLastCharQuote ? 1 : 0));
            value = ValueParser.parseString(stringValueWithoutQuote);
        } else if (!isFirstCharQuote || !isLastCharQuote) {
            throw new IllegalArgumentException("Unable to parse value");
        } else {
            String stringValueWithoutQuote = stringValue.substring(1, stringValue.length() - 1);
            value = ValueParser.parseString(stringValueWithoutQuote);
        }

        return new SearchPredicateCriteria<>(name, value, operator, conditionOperator);
    }
}
