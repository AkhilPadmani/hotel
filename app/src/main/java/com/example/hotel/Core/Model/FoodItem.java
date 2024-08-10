package com.example.hotel.Core.Model;

public class FoodItem {
    private String name;
    private String description;
    private double fullprice;
    private double halfPrice; // Added field for half price
    private String imageUrl;
    private String category;

    // Default constructor required for calls to DataSnapshot.getValue(FoodItem.class)
    public FoodItem() {
    }

    public FoodItem(String name, String description, double fullprice, double halfPrice, String imageUrl, String category) {
        this.name = name;
        this.description = description;
        this.fullprice = fullprice;
        this.halfPrice = halfPrice; // Initialize the half price field
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFullprice() {
        return fullprice;
    }

    public void setFullprice(double price) {
        this.fullprice = price;
    }

    public double getHalfPrice() {
        return halfPrice;
    }

    public void setHalfPrice(double halfPrice) {
        this.halfPrice = halfPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
