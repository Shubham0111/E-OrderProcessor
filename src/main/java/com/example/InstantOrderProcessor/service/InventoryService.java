package com.example.InstantOrderProcessor.service;

import com.example.InstantOrderProcessor.dao.OrderDao;
import com.example.InstantOrderProcessor.dto.EventOrderDto;
import com.example.InstantOrderProcessor.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "order-event", groupId = "inventory-group")
    public void handleOrder(EventOrderDto eventOrderDto) {
        try {
            if ("PLACED".equals(eventOrderDto.getEventType())) {
                Order order = orderDao.findById(eventOrderDto.getOrder().getId()).orElse(null);
                if(order != null) {
                    order.setStatus("ITEMS_PACKED");
                    orderDao.save(order);
                    kafkaTemplate.send("order-events", new EventOrderDto(eventOrderDto.getOrder(), "ITEMS_PACKED"));
                }
            }
        }
        catch(Exception e)
         {
             log.error("error while handeling the order {}, {}",e, Arrays.asList(e.getStackTrace()));
         }
    }

}
