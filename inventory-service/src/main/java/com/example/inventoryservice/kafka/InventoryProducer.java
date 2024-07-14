package com.example.inventoryservice.kafka;

import com.example.inventoryservice.kafka.event.InventoryResponseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryProducer {

    private static final Logger log = LoggerFactory.getLogger(InventoryProducer.class);
    private static final String TOPIC = "inventory-response";

    private final KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate;

    public InventoryProducer(KafkaTemplate<String, InventoryResponseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryResponse(InventoryResponseEvent event) {
        log.info("Sending InventoryResponseEvent for order {}: available={}",
                event.getOrderId(), event.isAvailable());
        kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);
    }

}
