package com.kiloux.restopos.model;

public class MenuItem {
    private int id;
    private String name;
    private String category;
    private double price;
    private int stock;
    private String imagePath;
    private String description;

    public MenuItem() {}

    public MenuItem(int id, String name, String category, double price, int stock, String imagePath, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.imagePath = imagePath;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
