package com.example.InstantOrderProcessor.service;

import com.example.InstantOrderProcessor.dao.*;
import com.example.InstantOrderProcessor.dto.*;
import com.example.InstantOrderProcessor.entity.*;
import com.example.InstantOrderProcessor.enums.ItemStatus;
import com.example.InstantOrderProcessor.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @Autowired
    CalculateScheduleService calculateScheduleService;

   @Transactional
   public PlaceOrderResponseDto placeOrder(PlaceOrderRequestDto requestDto) {
       PlaceOrderResponseDto placeOrderResponseDto = new PlaceOrderResponseDto();
       String error =  validatePlaceOrderBaseChecks(requestDto);
              if(!ObjectUtils.isEmpty(error)) {
                  placeOrderResponseDto.setMessage(error);
                  return new PlaceOrderResponseDto(null, "FAILED", 0.0, error);
              }
        Order order = new Order();
        order.setCustomerId(requestDto.getCustomerId());
        order.setShippingAddress(requestDto.getHouseNo() + ", " + requestDto.getArea() + ", " + requestDto.getState());
        order.setTotalAmount(requestDto.getAmount());
        order.setShippingMode(requestDto.getShippingMode());
        order.setStatus("PLACED");

        for (PlaceOrderRequestDto.Item item : requestDto.getItems()) {
            OrderItem orderItem = new OrderItem();
            Optional<Items> items = itemDao.findById(item.getItemId());
            if (items.isPresent()) {
                Items itemEntity = items.get();
                itemEntity.setUnitsAvailable(itemEntity.getUnitsAvailable() - item.getQuantity());
                itemDao.save(itemEntity);
            }
            orderItem.setItemId(item.getItemId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrderId(order.getId());
            orderItemDao.save(orderItem);
        }
        order = orderDao.save(order);
        // todo- update the kafka template paramaters
        kafkaTemplate.send("order-recieved",requestDto.toString(),0) ;
        return new PlaceOrderResponseDto(order.getId(), "PLACED", requestDto.getAmount(), "Order successfully placed.");
    }


    private String validatePlaceOrderBaseChecks(PlaceOrderRequestDto requestDto) {
       if(ObjectUtils.isEmpty(requestDto) || ObjectUtils.isEmpty(requestDto.getCustomerId()) || ObjectUtils.isEmpty(requestDto.getItems())) {
           return "Invalid request";
       }
        Optional<Customer> customer = customerDao.findById(requestDto.getCustomerId());
        Double itemsAmount = 0D;
        if(ObjectUtils.isEmpty(customer)) {
            return "Customer not found";
        }
        for(PlaceOrderRequestDto.Item item : requestDto.getItems()) {
            Optional<Items> items = itemDao.findById(item.getItemId());
            if(ObjectUtils.isEmpty(items)) {
                return "Item not found in inventory";
            }
            if(ItemStatus.DISCONTINUED.equals(items.get().getStatus())) {
                return "Items is discontinued";
            }
            itemsAmount += items.get().getPrice() * item.getQuantity();
        }
       if(Math.abs(itemsAmount - requestDto.getAmount()) > 0.001){
            return "AMOUNT_MISMATCH";
        }
       return "";
    }


    public OrderStatusResponseDto getOrderStatus(Long orderId) {
        Optional<Order> optionalOrder = orderDao.findById(orderId);

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Optional<List<OrderItem>> orderItem = Optional.ofNullable(orderItemDao.findAllByOrderId(order.getId()));

            List<OrderStatusResponseDto.OrderItemDto> orderItemDto = orderItem .map(orderItems -> orderItems.stream()
                    .map(item -> new OrderStatusResponseDto.OrderItemDto(
                            item.getId(),
                            item.getItemName(),
                            item.getQuantity(),
                            item.getItemPrice()
                    )).collect(Collectors.toList())).orElse(Collections.emptyList());

            OrderStatusResponseDto.OrderDetails orderDetails = new OrderStatusResponseDto.OrderDetails(
                    order.getCustomerId(),
                    order.getShippingAddress(),
                    orderItemDto
            );
           LocalDateTime eta = calculateScheduleService.calculateETA(order);
            return new OrderStatusResponseDto(
                    "success",
                    order.getStatus(),
                    eta,
                    orderDetails,
                    order.getTotalAmount(),
                    order.getShippingMode()
            );
        }
        else{
            throw new RuntimeException("Order not found");
        }
    }

    @Transactional
    public CancelOrderResponseDto cancelOrder(CancelOrderRequestDto cancelOrderRequestDto) {
        Optional<Order> optionalOrder = orderDao.findById(cancelOrderRequestDto.getOrderId());

        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();

            if (order.getStatus().equals(OrderStatus.SHIPPED.name()) || order.getStatus().equals(OrderStatus.DELIVERED.name())) {
                return new CancelOrderResponseDto("failed", "Order cannot be cancelled as it is already shipped or delivered",cancelOrderRequestDto.getOrderId());
            }
           List<OrderItem> orderItem = orderItemDao.findAllByOrderId(order.getId());
            for (OrderItem orderItems : orderItem) {
                Optional<Items> optionalItemEntity = itemDao.findById(orderItems.getItemId());
                if (optionalItemEntity.isPresent()) {
                    Items items = optionalItemEntity.get();
                    items.setUnitsAvailable(items.getUnitsAvailable() + orderItems.getQuantity());
                    itemDao.save(items);
                }
            }
            order.setStatus(OrderStatus.CANCELLED.name());
            orderDao.save(order);
            kafkaTemplate.send("order-events", new EventOrderDto(order, "CANCELLED"));
            return new CancelOrderResponseDto("success", "Order successfully cancelled", cancelOrderRequestDto.getOrderId());
    }
     else{
       return new CancelOrderResponseDto("failed", "Order not found",cancelOrderRequestDto.getOrderId());
        }


   }


}
