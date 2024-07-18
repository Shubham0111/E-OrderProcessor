package com.example.InstantOrderProcessor.dao;

import com.example.InstantOrderProcessor.entity.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemDao extends CrudRepository<OrderItem,Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    public List<OrderItem> findAllByOrderId(Long orderId);
}
