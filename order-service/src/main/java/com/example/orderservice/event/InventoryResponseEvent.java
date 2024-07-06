package com.example.orderservice.event;

public class InventoryResponseEvent {

    private Long orderId;
    private String productId;
    private boolean available;
    private String message;

    public InventoryResponseEvent() {
    }

    public InventoryResponseEvent(Long orderId, String productId, boolean available, String message) {
        this.orderId = orderId;
        this.productId = productId;
        this.available = available;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
