/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Admin
 */
    public class Product {
    private int productID;
    private String productName;
    private int categoryID;
    private String description;
    private double price;
    private String image;
    private String status;

    public Product() {}

    public Product(int productID, String productName, int categoryID, String description, double price, String image, String status) {
        this.productID = productID;
        this.productName = productName;
        this.categoryID = categoryID;
        this.description = description;
        this.price = price;
        this.image = image;
        this.status = status;
    }

   

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }
 
    public void setProductName(String productName) {
        this.productName = productName;
    }
 
    public int getCategoryID() {
        return categoryID;
    }
 
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public String getImage() {
        return image;
    }
 
    public void setImage(String image) {
        this.image = image;
    }
 
    public String getStatus() {
        return status;
    }
 
    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "Product{" + "productID=" + productID + ", productName=" + productName + ", categoryID=" + categoryID + ", description=" + description + ", image=" + image + ", status=" + status + '}';
    }
    
}
