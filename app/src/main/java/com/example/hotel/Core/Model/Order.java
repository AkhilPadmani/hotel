package com.example.hotel.Core.Model;

public class Order {

    private String tableNo;
    private String foodName;
    private int quantity;
    private double totalAmount;

    // Default constructor
    public Order() {}

    // Parameterized constructor
    public Order(String tableNo, String foodName, int quantity, double totalAmount) {
        this.tableNo = tableNo;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
