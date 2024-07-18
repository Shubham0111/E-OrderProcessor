package com.example.InstantOrderProcessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderResponseDto {
    String status;
    String message;
    Long orderId;
}
