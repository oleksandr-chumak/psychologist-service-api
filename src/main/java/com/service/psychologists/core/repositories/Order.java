package com.service.psychologists.core.repositories;

import com.service.psychologists.core.repositories.enums.OrderDirection;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private String name;

    private OrderDirection direction;
}
