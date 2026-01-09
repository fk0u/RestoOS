package com.kiloux.restopos.model;

public class Table {
    private int id;
    private int tableNumber;
    private int capacity;
    private String status; // AVAILABLE, OCCUPIED
    private int xPos;
    private int yPos;

    public Table() {}
    
    public Table(int id, int tableNumber, int capacity, String status) {
        this.id = id;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTableNumber() { return tableNumber; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getxPos() { return xPos; }
    public void setxPos(int xPos) { this.xPos = xPos; }
    public int getyPos() { return yPos; }
    public void setyPos(int yPos) { this.yPos = yPos; }
}
