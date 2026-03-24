package model;

import entity.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO extends DBContext {

    /* =====================================================
       1. CHECK: User đã mua sản phẩm chưa
    ===================================================== */
    public boolean hasUserPurchasedProduct(int userID, int productID) {

        String sql = """
            SELECT 1
            FROM dbo.Orders o
            JOIN dbo.OrderDetails od ON o.OrderID = od.OrderID
            JOIN dbo.ProductVariants pv ON od.VariantID = pv.VariantID
            WHERE o.UserID = ?
              AND pv.ProductID = ?
              AND o.Status IN ('Completed', 'Pending')
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setInt(2, productID);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
       2. CHECK: User đã review chưa
    ===================================================== */
    public boolean hasUserReviewed(int userID, int productID) {

        String sql = """
            SELECT 1 FROM dbo.Reviews
            WHERE UserID = ? AND ProductID = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userID);
            ps.setInt(2, productID);

            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
       3. INSERT REVIEW Thêm review mới.
    ===================================================== */
    public boolean insertReview(Review review) {

        if (!hasUserPurchasedProduct(review.getUserID(), review.getProductID())) {
            return false;
        }

        if (hasUserReviewed(review.getUserID(), review.getProductID())) {
            return false;
        }

        String sql = """
            INSERT INTO dbo.Reviews (ProductID, UserID, Rating, Comment, CreatedAt)
            VALUES (?, ?, ?, ?, GETDATE())
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getProductID());
            ps.setInt(2, review.getUserID());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* =====================================================
       4. GET REVIEW BY PRODUCT Lấy tất cả review của 1 sản phẩm.
    ===================================================== */
    public List<Review> getReviewsByProductID(int productID) {

        List<Review> list = new ArrayList<>();

        String sql = """
            SELECT r.*, u.Username, u.FullName
            FROM dbo.Reviews r
            JOIN dbo.Users u ON r.UserID = u.UserID
            WHERE r.ProductID = ?
            ORDER BY r.CreatedAt DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Review r = new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("ProductID"),
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("CreatedAt")
                );

                list.add(r);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    /* =====================================================
       5. GET ALL REVIEWS Lấy tất cả review trong hệ thống.
    ===================================================== */
    public List<Review> getAllReviews() {

        List<Review> list = new ArrayList<>();

        String sql = """
            SELECT r.*, u.Username, u.FullName
            FROM dbo.Reviews r
            JOIN dbo.Users u ON r.UserID = u.UserID
            ORDER BY r.CreatedAt DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Review r = new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("ProductID"),
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("CreatedAt")
                );

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /* =====================================================
       6. GET REVIEW BY ID Lấy 1 review theo ID.
    ===================================================== */
    public Review getReviewByID(int reviewID) {

        String sql = """
            SELECT r.*, u.Username, u.FullName
            FROM dbo.Reviews r
            JOIN dbo.Users u ON r.UserID = u.UserID
            WHERE r.ReviewID = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new Review(
                        rs.getInt("ReviewID"),
                        rs.getInt("ProductID"),
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("FullName"),
                        rs.getInt("Rating"),
                        rs.getString("Comment"),
                        rs.getTimestamp("CreatedAt")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* =====================================================
       7. UPDATE REVIEW
    ===================================================== */
//    public boolean updateReview(Review review) {
//
//        String sql = """
//            UPDATE dbo.Reviews
//            SET Rating = ?, Comment = ?
//            WHERE ReviewID = ?
//        """;
//
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, review.getRating());
//            ps.setString(2, review.getComment());
//            ps.setInt(3, review.getReviewID());
//
//            return ps.executeUpdate() > 0;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    /* =====================================================
       8. DELETE REVIEW Xóa 1 review theo ID.
    ===================================================== */
    public boolean deleteReview(int reviewID) {

        String sql = "DELETE FROM dbo.Reviews WHERE ReviewID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reviewID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /* =====================================================
       9. DELETE REVIEW BY PRODUCT Xóa tất cả review của một sản phẩm.
    ===================================================== */
    public void deleteReviewsByProductID(int productID) {

        String sql = "DELETE FROM dbo.Reviews WHERE ProductID = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* =====================================================
       10. GET AVERAGE RATING Tính điểm đánh giá trung bình của sản phẩm.
    ===================================================== */
    public double getAverageRating(int productID) {

        String sql = """
            SELECT AVG(CAST(Rating AS FLOAT))
            FROM dbo.Reviews
            WHERE ProductID = ?
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}