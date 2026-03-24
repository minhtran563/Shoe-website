package entity;

import java.util.Date;

public class Review {

    private int reviewID;
    private int productID;
    private int userID;
    private String username;
    private String fullname;
    private int rating;
    private String comment;
    private Date createdAt;

    public Review() {
    }

    // Constructor đầy đủ chuẩn (thứ tự đúng)
    public Review(int reviewID, int productID, int userID,
                  String username, String fullname,
                  int rating, String comment, Date createdAt) {

        this.reviewID = reviewID;
        this.productID = productID;
        this.userID = userID;
        this.username = username;
        this.fullname = fullname;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // ================= GETTER & SETTER =================

    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", productID=" + productID +
                ", userID=" + userID +
                ", username='" + username + '\'' +
                ", fullname='" + fullname + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}