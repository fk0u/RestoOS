package com.kiloux.restopos.model;

public class CartItem {
    private MenuItem menuItem;
    private int quantity;
    private String notes;

    public CartItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.notes = "";
    }

    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public double getSubtotal() {
        return menuItem.getPrice() * quantity;
    }
}
