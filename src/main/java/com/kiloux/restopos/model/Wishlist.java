package com.kiloux.restopos.model;

import java.time.LocalDateTime;

public class Wishlist {
    private int id;
    private int userId;
    private int menuItemId;
    private LocalDateTime addedAt;

    public Wishlist() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getMenuItemId() { return menuItemId; }
    public void setMenuItemId(int menuItemId) { this.menuItemId = menuItemId; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
