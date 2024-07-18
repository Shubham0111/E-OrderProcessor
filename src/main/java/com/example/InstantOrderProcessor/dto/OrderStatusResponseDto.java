package com.example.InstantOrderProcessor.dto;
import com.example.InstantOrderProcessor.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusResponseDto {
    String status;
    String orderStatus;
    LocalDateTime eta;
    OrderDetails orderDetails;
    Double totalAmount;
    String shippingMode;
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class OrderDetails {
        Long customerId;
        String shippingAddress;
        List<OrderItemDto> orderItems;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDto {
        private Long itemId;
        private String itemName;
        private Long quantity;
        private Double price;
    }


}
