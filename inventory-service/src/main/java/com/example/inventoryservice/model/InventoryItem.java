package com.example.inventoryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Min(0)
    @Column(nullable = false)
    private Integer quantity;

    @Min(0)
    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    public InventoryItem() {
        this.quantity = 0;
        this.reservedQuantity = 0;
    }

    public InventoryItem(String productId, String productName, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.reservedQuantity = 0;
    }

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

}
