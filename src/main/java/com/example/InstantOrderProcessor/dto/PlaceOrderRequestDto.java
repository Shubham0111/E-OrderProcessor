package com.example.InstantOrderProcessor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class PlaceOrderRequestDto {
      private  Long customerId;
      private  List<Item> items;
      private  String houseNo;
      private  String area;
      private  String state;
      private  Double amount;
      private  String shippingMode;

      @Data
      @AllArgsConstructor
      public static class Item {
         Long itemId;
         Long quantity;
      }
}
