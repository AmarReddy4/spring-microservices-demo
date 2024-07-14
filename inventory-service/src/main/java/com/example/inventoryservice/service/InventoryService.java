package com.example.inventoryservice.service;

import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryItem> getAllItems() {
        return inventoryRepository.findAll();
    }

    public Optional<InventoryItem> getByProductId(String productId) {
        return inventoryRepository.findByProductId(productId);
    }

    @Transactional
    public boolean checkAndReserveStock(String productId, int requestedQuantity) {
        Optional<InventoryItem> itemOpt = inventoryRepository.findByProductId(productId);

        if (itemOpt.isEmpty()) {
            log.warn("Product {} not found in inventory", productId);
            return false;
        }

        InventoryItem item = itemOpt.get();
        int available = item.getAvailableQuantity();

        if (available >= requestedQuantity) {
            item.setReservedQuantity(item.getReservedQuantity() + requestedQuantity);
            inventoryRepository.save(item);
            log.info("Reserved {} units of product {}", requestedQuantity, productId);
            return true;
        }

        log.warn("Insufficient stock for product {}: requested={}, available={}",
                productId, requestedQuantity, available);
        return false;
    }

    @Transactional
    public InventoryItem restockProduct(String productId, int quantity) {
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseGet(() -> {
                    InventoryItem newItem = new InventoryItem();
                    newItem.setProductId(productId);
                    newItem.setProductName(productId);
                    newItem.setQuantity(0);
                    newItem.setReservedQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        item = inventoryRepository.save(item);

        log.info("Restocked product {}: added {} units, new total={}",
                productId, quantity, item.getQuantity());
        return item;
    }

}
