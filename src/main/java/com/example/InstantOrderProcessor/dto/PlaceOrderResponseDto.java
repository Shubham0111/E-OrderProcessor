package com.example.InstantOrderProcessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class PlaceOrderResponseDto {
    private Long orderId;
    private String status;
    private Double totalAmount;
    private String message;
}
