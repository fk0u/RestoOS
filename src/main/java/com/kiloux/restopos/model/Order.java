package com.kiloux.restopos.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int tableId;
    private String orderType; // DINE_IN, TAKEAWAY
    private String status;
    private double totalAmount;
    private LocalDateTime createdAt;
    private String paymentStatus;
    private String customerName;
    private int queueNumber;
    private java.util.List<CartItem> items; // Transient for Kitchen View
    
    // New Fields
    private String notes;
    private double discount;
    private String voucherCode;

    public Order() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public int getQueueNumber() { return queueNumber; }
    public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }

    public java.util.List<CartItem> getItems() { return items; }
    public void setItems(java.util.List<CartItem> items) { this.items = items; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }
}
