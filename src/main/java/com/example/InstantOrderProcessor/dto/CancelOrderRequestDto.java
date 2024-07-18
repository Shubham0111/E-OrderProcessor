package com.example.InstantOrderProcessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequestDto {
    Long customerId;
    Long orderId;
    String reason;
}
