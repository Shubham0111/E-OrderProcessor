package com.example.InstantOrderProcessor.service;

import com.example.InstantOrderProcessor.dto.EventOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEventListener {

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void handleOrderEvent(EventOrderDto orderEvent) {
        notificationService.processOrderEvent(orderEvent);
    }
}