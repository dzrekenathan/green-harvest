package com.exams.farmingsystem.examsproject.models;


import java.time.LocalDate;

public class Sales {
    private int saleId;
    private int productId;
    private String productName;
    private LocalDate saleDate;
    private int quantity;
    private double amount;

    // Constructor
    public Sales(int saleId, int productId, String productName, LocalDate saleDate, int quantity, double amount) {
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.amount = amount;
    }

    // Getters and Setters
    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
