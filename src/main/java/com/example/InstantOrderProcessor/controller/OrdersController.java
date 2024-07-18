package com.example.InstantOrderProcessor.controller;

import com.example.InstantOrderProcessor.dto.*;
import com.example.InstantOrderProcessor.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

      @Autowired
      OrderService orderService;

      @PostMapping(value = "/place",produces = "application/json",consumes = "application/json")
      ResponseEntity<?> placeOrder(@RequestBody PlaceOrderRequestDto requestDto) {
            try{
                  PlaceOrderResponseDto placeOrderResponseDto = orderService.placeOrder(requestDto);
                  return ResponseEntity.ok(placeOrderResponseDto);
            }
            catch (IllegalArgumentException ex) {
              log.error("Validation Error",ex);
                  return ResponseEntity.badRequest().body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "Validation Error", ex.getMessage()));
            }
            catch (Exception e) {
                  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage()));
            }
      }

      @GetMapping(value = "/status/{orderId}",produces = "application/json")
      ResponseEntity<?> OrderStatus(@PathVariable Long orderId) {
         OrderStatusResponseDto orderStatusResponseDto =  orderService.getOrderStatus(orderId);
         return ResponseEntity.ok(orderStatusResponseDto);
      }

      @PostMapping(value = "/cancel" , consumes = "application/json" , produces = "application/json")
      ResponseEntity<?> cancelOrder(@RequestBody CancelOrderRequestDto cancelOrderRequestDto) {
         CancelOrderResponseDto cancelOrderResponseDto = orderService.cancelOrder(cancelOrderRequestDto);
            return ResponseEntity.ok(cancelOrderResponseDto);

      }



}
