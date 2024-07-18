package com.example.InstantOrderProcessor.service;

import com.example.InstantOrderProcessor.dao.OrderDao;
import com.example.InstantOrderProcessor.dto.EventOrderDto;
import com.example.InstantOrderProcessor.entity.Order;
import com.example.InstantOrderProcessor.enums.ShippingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
public class ShippingService {

    @Autowired
    CalculateScheduleService calculateScheduleService;

    @Autowired
    OrderDao orderDao;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @KafkaListener(topics="order-events",groupId = "shipping-group")
    public void handleOrderEvent(EventOrderDto eventOrderDto) {
        try {
            Order order = orderDao.findById(eventOrderDto.getOrder().getId()).orElse(null);
            if ("ITEMS_PACKED".equals(eventOrderDto.getEventType()) && !ObjectUtils.isEmpty(order)) {
                initiateShipment(order);
            }
            processShipping(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initiateShipment(Order order) {
        ShippingMode shippingMode = ShippingMode.valueOf(order.getShippingMode());
        LocalDateTime eta = calculateScheduleService.calculateETA(order);

    }

    private void processShipping(Order order) {

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                updateOrderStatus(order, "IN_TRANSIT");
                Thread.sleep(5000);
                updateOrderStatus(order, "OUT_FOR_DELIVERY");
                Thread.sleep(5000);
                updateOrderStatus(order, "DELIVERED");
            } catch (InterruptedException e) {

            }
        }).start();
    }

    private void updateOrderStatus(Order order, String status) {
        order.setStatus(status);
        orderDao.save(order);
        kafkaTemplate.send("order-events", new EventOrderDto(order, status));

        // notificationService.notifyUser(order.getCustomerId(), "Your order status: " + status);
    }

}
