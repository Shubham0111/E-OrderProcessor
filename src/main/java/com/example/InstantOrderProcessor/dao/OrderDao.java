package com.example.InstantOrderProcessor.dao;

import com.example.InstantOrderProcessor.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order,Long> {
}
