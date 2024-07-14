package com.example.inventoryservice.kafka;

import com.example.inventoryservice.kafka.event.InventoryResponseEvent;
import com.example.inventoryservice.kafka.event.OrderCreatedEvent;
import com.example.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryService inventoryService;
    private final InventoryProducer inventoryProducer;

    public InventoryConsumer(InventoryService inventoryService, InventoryProducer inventoryProducer) {
        this.inventoryService = inventoryService;
        this.inventoryProducer = inventoryProducer;
    }

    @KafkaListener(topics = "order-created", groupId = "inventory-service-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("Received OrderCreatedEvent for order {}, product {}, quantity {}",
                event.getOrderId(), event.getProductId(), event.getQuantity());

        boolean reserved = inventoryService.checkAndReserveStock(
                event.getProductId(), event.getQuantity());

        InventoryResponseEvent response;
        if (reserved) {
            response = new InventoryResponseEvent(
                    event.getOrderId(),
                    event.getProductId(),
                    true,
                    "Stock reserved successfully"
            );
        } else {
            response = new InventoryResponseEvent(
                    event.getOrderId(),
                    event.getProductId(),
                    false,
                    "Insufficient stock for product " + event.getProductId()
            );
        }

        inventoryProducer.sendInventoryResponse(response);
    }

}
