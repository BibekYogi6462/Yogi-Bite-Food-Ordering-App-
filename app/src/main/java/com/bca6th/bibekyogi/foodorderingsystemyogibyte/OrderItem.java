package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

public class OrderItem {
    private String foodName;
    private int quantity;
    private double price;

    // Constructors
    public OrderItem() {}

    public OrderItem(String foodName, int quantity, double price) {
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTotal() { return quantity * price; }
}