package com.example.hotel.Core.Model;

public class FoodData {
    private String foodImage;
    private String name;
    private String description;
    private String fullprice,halfprice;

    // Constructor
    public FoodData(String foodImage, String name, String description, String fullprice,String halfprice) {
        this.foodImage = foodImage;
        this.name = name;
        this.description = description;
        this.fullprice = fullprice;
        this.halfprice = halfprice;
    }

    // Getters and setters
    public String getFoodImage() { return foodImage; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getFullPrice() { return fullprice; }
    public String getHalfPrice() { return halfprice; }
}
