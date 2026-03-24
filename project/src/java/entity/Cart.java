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
public class Cart {
    private int cartID;
    private User users;
    private String status;
    private Date createdAt;

    public Cart() {
    }

    public Cart(int cartID, User users, String status, Date createdAt) {
        this.cartID = cartID;
        this.users = users;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getCartID() {
        return cartID;
    }

    public void setCartID(int cartID) {
        this.cartID = cartID;
    }

    public User getUsers() {
        return users;
    }

    public void setUsers(User users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Cart{" + "cartID=" + cartID + ", users=" + users + ", status=" + status + ", createdAt=" + createdAt + '}';
    }
    
}
