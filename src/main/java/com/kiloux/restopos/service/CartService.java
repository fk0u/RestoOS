package com.kiloux.restopos.service;

import com.kiloux.restopos.model.CartItem;
import com.kiloux.restopos.model.MenuItem;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private static CartService instance;
    private List<CartItem> items;
    private int selectedTableId = -1;
    private List<Runnable> listeners = new ArrayList<>();

    private CartService() {
        items = new ArrayList<>();
    }
    
    public void addListener(Runnable listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners() {
        for (Runnable r : listeners) r.run();
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
        // Merge qty if same item+notes, otherwise add new line item
        for (CartItem cartItem : items) {
            if (cartItem.getMenuItem().getId() == item.getId() && cartItem.getNotes().equals(notes)) {
                cartItem.setQuantity(cartItem.getQuantity() + qty);
                notifyListeners();
                return;
            }
        }
        CartItem newItem = new CartItem(item, qty);
        newItem.setNotes(notes);
        items.add(newItem);
        notifyListeners();
    }

    public void removeItem(MenuItem item) {
        items.removeIf(i -> i.getMenuItem().getId() == item.getId());
        notifyListeners();
    }
    
    public void updateQuantity(MenuItem item, int qty) {
         if (qty <= 0) {
             removeItem(item);
             return;
         }
         for (CartItem cartItem : items) {
            if (cartItem.getMenuItem().getId() == item.getId()) {
                cartItem.setQuantity(qty);
                notifyListeners();
                return;
            }
        }
    }

    public List<CartItem> getItems() { return items; }
    
    public void clear() {
        items.clear();
        notifyListeners();
    }

    public void clearCart() {
        clear();
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
