/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Admin
 */
public class OrderDetail {

    private int orderID;
    private int variantID;
    private int quantity;
    private double unitPrice;
    private String productName;
    private String color;
    private String size;

    public OrderDetail() {
    }

    public OrderDetail(int orderID, int variantID, int quantity, double unitPrice, String productName, String color, String size) {
        this.orderID = orderID;
        this.variantID = variantID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.productName = productName;
        this.color = color;
        this.size = size;
    }

    

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getVariantID() {
        return variantID;
    }

    public void setVariantID(int variantID) {
        this.variantID = variantID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "orderID=" + orderID + ", variantID=" + variantID + ", quantity=" + quantity + ", unitPrice=" + unitPrice + ", productName=" + productName + ", color=" + color + ", size=" + size + '}';
    }
    

 
}
