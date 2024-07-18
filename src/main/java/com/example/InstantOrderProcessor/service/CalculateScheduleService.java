package com.example.InstantOrderProcessor.service;

import com.example.InstantOrderProcessor.entity.Order;
import com.example.InstantOrderProcessor.enums.ShippingMode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CalculateScheduleService {

    public LocalDateTime calculateETA(Order order) {
        ShippingMode shippingMode = ShippingMode.valueOf(order.getShippingMode());
        String deliveryAddress = order.getShippingAddress();
        int distance = calcualateDistance(deliveryAddress,order.getStatus());
        LocalDateTime estimatedDays;
        int packagingDays = 0;
        if ("ITEMS_PACKED".equals(order.getStatus()) || "PLACED".equals(order.getStatus())) {
            // Order is still in inventory
            if (ShippingMode.EXPRESS.equals(shippingMode)) {
                packagingDays = 1;
            } else {
                packagingDays = 2;
            }
        }
        switch (shippingMode) {
            case EXPRESS:
                int expressOrder = packagingDays +(distance/300);
                estimatedDays = LocalDateTime.now().plusDays(expressOrder);
                break;
            case STANDARD:
            default:
                int defaultOrder = packagingDays + (distance/100);
                estimatedDays = LocalDateTime.now().plusDays(defaultOrder);
                break;
        }
        return estimatedDays;

    }

    public int calcualateDistance(String deliveryAddress,String orderStatus) {
        // todo- add some logic here later
        return 300;
    }
}
