package com.example.InstantOrderProcessor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order")
@Getter
@Setter
public class Order extends BaseEntity{
    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "shipping_address")
    private String shippingAddress;
    @Column(name = "total_amount")
    private Double totalAmount;
    @Column(name = "status")
    private String status;
    @Column(name = "shipping_mode")
    private String shippingMode;
}
