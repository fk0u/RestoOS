package com.kiloux.restopos.service;

import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private static CartService instance;
    private List<CartItem> items;
    private int selectedTableId = -1;

    private CartService() {
        items = new ArrayList<>();
    }

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    public void addItem(MenuItem item) {
        addItemWithNotes(item, 1, "");
    }

    public void addItemWithNotes(MenuItem item, int qty, String notes) {
        // Check if item with same ID AND same notes exists? 
        // For simplicity, we treat different notes as different line items?
        // Or just append. For this prototype, separate lines if notes differ is better, 
        // but current equals logic might rely on Item ID.
        // Let's simple add new item if notes differ, or update qty if same.
        
        for (CartItem cartItem : items) {
            if (cartItem.getMenuItem().getId() == item.getId() && cartItem.getNotes().equals(notes)) {
                cartItem.setQuantity(cartItem.getQuantity() + qty);
                return;
            }
        }
        CartItem newItem = new CartItem(item, qty);
        newItem.setNotes(notes);
        items.add(newItem);
    }

    public void removeItem(MenuItem item) {
        items.removeIf(i -> i.getMenuItem().getId() == item.getId());
    }
    
    public void updateQuantity(MenuItem item, int qty) {
         if (qty <= 0) {
             removeItem(item);
             return;
         }
         for (CartItem cartItem : items) {
            if (cartItem.getMenuItem().getId() == item.getId()) {
                cartItem.setQuantity(qty);
                return;
            }
        }
    }

    public List<CartItem> getItems() { return items; }
    
    public void clear() {
        items.clear();
    }
    
    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
    
    public double getTax() {
        return getSubtotal() * 0.11;
    }
    
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    public void setSelectedTableId(int tableId) {
        this.selectedTableId = tableId;
    }
    public int getSelectedTableId() {
        return selectedTableId;
    }
}
