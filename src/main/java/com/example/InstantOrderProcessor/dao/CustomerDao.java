package com.example.InstantOrderProcessor.dao;

import com.example.InstantOrderProcessor.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDao extends CrudRepository<Customer,Long> {

}
