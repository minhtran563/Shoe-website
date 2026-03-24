/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class Order {

    private int orderID;
    private int userID;
    private Date orderDate;
    private double totalPrice;
    private String paymentMethod;
    private String paymentStatus;
    private String status;
    private String shippingAddress;   // thêm
    private String phoneNumber; 

    public Order() {
    }

    public Order(int orderID, int userID, Date orderDate, double totalPrice, String paymentMethod, String paymentStatus, String status, String shippingAddress, String phoneNumber) {
        this.orderID = orderID;
        this.userID = userID;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", userID=" + userID + ", orderDate=" + orderDate + ", totalPrice=" + totalPrice + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus + ", status=" + status + ", shippingAddress=" + shippingAddress + ", phoneNumber=" + phoneNumber + '}';
    }

}
