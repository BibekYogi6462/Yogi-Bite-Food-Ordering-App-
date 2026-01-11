package com.bca6th.bibekyogi.foodorderingsystemyogibyte;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {
    private long id;
    private String orderDate;
    private String orderTime;
    private double totalAmount;
    private String status;
    private String deliveryAddress;
    private String paymentMethod;
    private String phoneNumber;
    private List<OrderItem> foodItems; // Add this

    // Constructors
    public Order() {
        foodItems = new ArrayList<>(); // Initialize the list
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }

    public String getOrderTime() { return orderTime; }
    public void setOrderTime(String orderTime) { this.orderTime = orderTime; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // Food items methods
    public List<OrderItem> getFoodItems() { return foodItems; }
    public void setFoodItems(List<OrderItem> foodItems) { this.foodItems = foodItems; }
    public void addFoodItem(OrderItem item) { this.foodItems.add(item); }
}