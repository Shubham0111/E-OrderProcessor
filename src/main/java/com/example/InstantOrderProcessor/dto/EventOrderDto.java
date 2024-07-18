package com.example.InstantOrderProcessor.dto;

import com.example.InstantOrderProcessor.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventOrderDto {
    private Order order;
    private String eventType;
    private OrderStatusResponseDto orderStatusResponseDto;
}
