package com.example.InstantOrderProcessor.service;


import com.example.InstantOrderProcessor.dto.EventOrderDto;
import com.example.InstantOrderProcessor.dto.NotificationEvent;
import com.example.InstantOrderProcessor.dto.OrderStatusResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class NotificationService {

    @Autowired
    NotificationService notificationService;

    @Autowired
    KafkaTemplate kafkaTemplate;

    public void processOrderEvent(EventOrderDto orderEvent) {
        String eventType = orderEvent.getEventType();
        OrderStatusResponseDto orderStatusResponseDto = orderEvent.getOrderStatusResponseDto();
        switch (eventType) {
            case "ORDER_PLACED":
                sendNotification(orderStatusResponseDto, "Your order has been placed.");
                break;
            case "ITEMS_PACKED":

                sendNotification(orderStatusResponseDto, "Your items have been packed.");
                break;
            case "ORDER_SHIPPED":

                sendNotification(orderStatusResponseDto, "Your order has been shipped.");
                break;
            case "ORDER_DELIVERED":

                sendNotification(orderStatusResponseDto, "Your order has been delivered.");
                break;
        }

    }
    private void sendNotification(OrderStatusResponseDto orderStatusResponseDto, String message) {
        NotificationEvent notificationEvent = new NotificationEvent();
        notificationEvent.setCustomerId(orderStatusResponseDto.getOrderDetails().getCustomerId());
        notificationEvent.setMessage(message);

        kafkaTemplate.send("notification-events", notificationEvent);
    }
}
