package com.example.InstantOrderProcessor.dao;

import com.example.InstantOrderProcessor.entity.Items;
import org.springframework.data.repository.CrudRepository;

public interface ItemDao extends CrudRepository<Items,Long> {
}
