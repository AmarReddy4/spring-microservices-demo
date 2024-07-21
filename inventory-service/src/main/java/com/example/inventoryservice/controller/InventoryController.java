package com.example.inventoryservice.controller;

import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllItems());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItem> getByProductId(@PathVariable String productId) {
        return inventoryService.getByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{productId}/restock")
    public ResponseEntity<InventoryItem> restockProduct(
            @PathVariable String productId,
            @RequestParam int quantity) {
        InventoryItem item = inventoryService.restockProduct(productId, quantity);
        return ResponseEntity.ok(item);
    }

}
