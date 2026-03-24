package model;

import entity.ProductVariant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductVariantDAO extends DBContext {

    private Connection conn;

    /* ================= CONSTRUCTOR ================= */

    // Dùng cho các chức năng bình thường
    public ProductVariantDAO() {
        this.conn = super.getConnection(); // 🔥 FIX DUY NHẤT
    }

    // Dùng cho CHECKOUT (transaction)
    public ProductVariantDAO(Connection conn) {
        this.conn = conn;
    }

    /* ================= GET ================= */

    // Lấy tất cả variants trong bảng ProductVariants.
    public List<ProductVariant> getAllVariants() {
        List<ProductVariant> list = new ArrayList<>();
        String sql = """
                     SELECT VariantID, ProductID, Size, Color, Price, Stock
                     FROM ProductVariants""";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ProductVariant(
                        rs.getInt("VariantID"),
                        rs.getInt("ProductID"),
                        rs.getString("Size"),
                        rs.getString("Color"),
                        rs.getDouble("Price"),
                        rs.getInt("Stock")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy tất cả variant của 1 product.
    public List<ProductVariant> getVariantsByProductID(int productID) {
        List<ProductVariant> list = new ArrayList<>();
        String sql = "SELECT * FROM ProductVariants WHERE ProductID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ProductVariant(
                            rs.getInt("VariantID"),
                            rs.getInt("ProductID"),
                            rs.getString("Size"),
                            rs.getString("Color"),
                            rs.getDouble("Price"),
                            rs.getInt("Stock")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Lấy 1 variant theo ID.
    public ProductVariant getVariantByID(int variantID) {
        String sql = "SELECT * FROM ProductVariants WHERE VariantID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variantID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductVariant(
                            rs.getInt("VariantID"),
                            rs.getInt("ProductID"),
                            rs.getString("Size"),
                            rs.getString("Color"),
                            rs.getDouble("Price"),
                            rs.getInt("Stock")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* ================= INSERT ================= */

    // 4.Thêm variant mới cho product.
    public boolean insertVariant(ProductVariant pv) {
        String sql = """
            INSERT INTO ProductVariants (ProductID, Size, Color, Price, Stock)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pv.getProductID());
            ps.setString(2, pv.getSize());
            ps.setString(3, pv.getColor());
            ps.setDouble(4, pv.getPrice());
            ps.setInt(5, pv.getStock());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= UPDATE ================= */

    // 5. Cập nhật thông tin variant.
    public boolean updateVariant(ProductVariant pv) {
        String sql = """
            UPDATE ProductVariants
            SET Size = ?, Color = ?, Price = ?, Stock = ?
            WHERE VariantID = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pv.getSize());
            ps.setString(2, pv.getColor());
            ps.setDouble(3, pv.getPrice());
            ps.setInt(4, pv.getStock());
            ps.setInt(5, pv.getVariantID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= DELETE ================= */

    // 6. Xóa 1 variant.
    public boolean deleteVariant(int variantID) {
        String sql = "DELETE FROM ProductVariants WHERE VariantID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variantID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* ================= STOCK LOGIC (QUAN TRỌNG) ================= */

    // 7. Kiểm tra tồn kho có đủ không.
    public boolean isEnoughStock(int variantID, int quantity) {
        String sql = "SELECT Stock FROM ProductVariants WHERE VariantID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variantID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Stock") >= quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 8. Trừ kho (dùng trong CHECKOUT – có transaction)
//    Trừ kho khi checkout.
    public void decreaseStock(int variantID, int quantity) throws SQLException {
        String sql = """
            UPDATE ProductVariants
            SET Stock = Stock - ?
            WHERE VariantID = ? AND Stock >= ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, variantID);
            ps.setInt(3, quantity);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Không đủ số lượng trong kho (VariantID = " + variantID + ")");
            }
        }
    }
//    Xóa tất cả variant của product.
    public void deleteVariantsByProductID(int productID) {

    String sql = "DELETE FROM ProductVariants WHERE ProductID = ?";

    try (PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, productID);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static void main(String[] args) {

        int productID = 3; // 👉 đổi thành ID bạn muốn test

        try {

            ReviewDAO rDao = new ReviewDAO();
            ProductVariantDAO vDao = new ProductVariantDAO();
            OrderDetailDAO odDao = new OrderDetailDAO();
            ProductDAO pDao = new ProductDAO();

            System.out.println("===== START DELETE PRODUCT =====");

            // 1️⃣ Xóa review
            rDao.deleteReviewsByProductID(productID);
            System.out.println("✔ Deleted reviews");

            // 2️⃣ Lấy tất cả variant của product
            List<ProductVariant> variants = vDao.getVariantsByProductID(productID);

            // 3️⃣ Xóa OrderDetails theo từng variant
            for (ProductVariant v : variants) {
                odDao.deleteOrderDetailsByVariantID(v.getVariantID());
                System.out.println("✔ Deleted order details for variant: " + v.getVariantID());
            }

            // 4️⃣ Xóa variant
            vDao.deleteVariantsByProductID(productID);
            System.out.println("✔ Deleted variants");

            // 5️⃣ Xóa product
            boolean success = pDao.deleteProduct(productID);

            if (success) {
                System.out.println("🎉 PRODUCT DELETED SUCCESSFULLY");
            } else {
                System.out.println("❌ DELETE FAILED");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}