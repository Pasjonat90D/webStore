package com.myProject.webStore.domain.repository;

import com.myProject.webStore.domain.Order;

public interface OrderRepository {

    Long saveOrder(Order order);
}
