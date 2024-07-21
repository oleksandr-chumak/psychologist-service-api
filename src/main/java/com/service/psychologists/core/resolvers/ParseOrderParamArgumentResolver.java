package com.service.psychologists.core.resolvers;

import com.service.psychologists.core.annotations.ParsedOrderRequestParam;
import com.service.psychologists.core.repositories.Order;
import com.service.psychologists.core.repositories.enums.OrderDirection;
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
public class ParseOrderParamArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ParsedOrderRequestParam.class);
    }

    @Override
    public List<Order> resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        ParsedOrderRequestParam annotation = parameter.getParameterAnnotation(ParsedOrderRequestParam.class);

        if (annotation == null) {
            return null;
        }

        String paramName = annotation.name();
        String[] allowedValues = annotation.allowedValues();
        String paramValue = webRequest.getParameter(paramName);

        if (paramValue == null) {
            return new ArrayList<>();
        }

        String[] ordersString = paramValue.split(",");

        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < ordersString.length; i++) {
            String orderString = ordersString[i];
            try {
                orders.add(parseOrderStringToOrder(orderString));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order " + i + ": " + e.getMessage());
            }
        }

        for (Order order : orders) {
            Arrays.stream(allowedValues)
                    .filter(value -> value.equals(order.getName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order with name " + order.getName() + " is not allowed"));
        }


        return orders;
    }

    public Order parseOrderStringToOrder(String orderString) throws IllegalArgumentException {
        String[] parsedOrderString = orderString.split(" ");

        if (parsedOrderString.length == 0) {
            throw new IllegalArgumentException("Order name is required");
        }

        if (parsedOrderString.length > 2) {
            throw new IllegalArgumentException("Order must contains only name and order direction");
        }

        String name = parsedOrderString[0];
        OrderDirection direction;

        try {
            direction = parsedOrderString.length == 2 ? OrderDirection.valueOf(parsedOrderString[1]) : OrderDirection.ASC;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Order direction must be either asc or desc");
        }

        return new Order(name, direction);


    }
}
