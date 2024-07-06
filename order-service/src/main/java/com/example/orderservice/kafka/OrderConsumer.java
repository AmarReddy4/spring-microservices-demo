package com.example.orderservice.kafka;

import com.example.orderservice.event.InventoryResponseEvent;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);

    private final OrderService orderService;

    public OrderConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "inventory-response", groupId = "order-service-group")
    public void handleInventoryResponse(InventoryResponseEvent event) {
        log.info("Received inventory response for order {}: available={}",
                event.getOrderId(), event.isAvailable());

        if (event.isAvailable()) {
            orderService.updateOrderStatus(event.getOrderId(), OrderStatus.CONFIRMED);
        } else {
            orderService.updateOrderStatus(event.getOrderId(), OrderStatus.REJECTED);
        }
    }

}
