package com.example.hotel.Core.Model;

public class AddedData {
    private String foodName;
    private String itemName;
    private int quantity;
    private double piecePrice;
    private double totalAmount;
    private boolean isFullPrice;
    private String tableNo;  // New field

    public AddedData() {
        // Required empty constructor for Firestore
    }

    public AddedData(String foodName, String itemName, int quantity, double piecePrice, double totalAmount, boolean isFullPrice) {
        this.foodName = foodName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.piecePrice = piecePrice;
        this.totalAmount = totalAmount;
        this.isFullPrice = isFullPrice;
    }

    public AddedData(String foodName, String itemName, int quantity, double piecePrice, double totalAmount, boolean isFullPrice, String tableNo) {
        this.foodName = foodName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.piecePrice = piecePrice;
        this.totalAmount = totalAmount;
        this.isFullPrice = isFullPrice;
        this.tableNo = tableNo;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPiecePrice() {
        return piecePrice;
    }

    public void setPiecePrice(double piecePrice) {
        this.piecePrice = piecePrice;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isFullPrice() {
        return isFullPrice;
    }

    public void setFullPrice(boolean fullPrice) {
        isFullPrice = fullPrice;
    }
}
