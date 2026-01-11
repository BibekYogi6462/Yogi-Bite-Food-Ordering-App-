package com.bca6th.bibekyogi.foodorderingsystemyogibyte;


public class Offer {
    private String title;
    private String description;
    private String code;
    private int imageRes;
    private String color;

    public Offer(String title, String description, String code, int imageRes, String color) {
        this.title = title;
        this.description = description;
        this.code = code;
        this.imageRes = imageRes;
        this.color = color;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCode() { return code; }
    public int getImageRes() { return imageRes; }
    public String getColor() { return color; }
}