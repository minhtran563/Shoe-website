/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Admin
 */
public class ProductVariant {
    private int variantID;
    private int productID;
    private String size;
    private String color;
    private double price;
    private int stock;

    public ProductVariant() {
    }

    public ProductVariant(int variantID, int productID, String size,
                          String color, double price, int stock) {
        this.variantID = variantID;
        this.productID = productID;
        this.size = size;
        this.color = color;
        this.price = price;
        this.stock = stock;
    }

    public int getVariantID() {
        return variantID;
    }

    public void setVariantID(int variantID) {
        this.variantID = variantID;
    }

    public int getProductID() {
        return productID;
    }
 
    public void setProductID(int productID) {
        this.productID = productID;
    }
 
    public String getSize() {
        return size;
    }
 
    public void setSize(String size) {
        this.size = size;
    }
 
    public String getColor() {
        return color;
    }
 
    public void setColor(String color) {
        this.color = color;
    }
 
    public double getPrice() {
        return price;
    }
 
    public void setPrice(double price) {
        this.price = price;
    }
 
    public int getStock() {
        return stock;
    }
 
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "ProductVariant{" + "variantID=" + variantID + ", productID=" + productID + ", size=" + size + ", color=" + color + ", price=" + price + ", stock=" + stock + '}';
    }
    
    
}
