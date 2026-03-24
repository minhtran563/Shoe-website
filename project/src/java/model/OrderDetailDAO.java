package model;

import entity.OrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO extends DBContext {

    private final Connection conn;

    public OrderDetailDAO() {
        this.conn = super.getConnection();
    }

    public OrderDetailDAO(Connection conn) {
        this.conn = conn;
    }

//    Lấy chi tiết sản phẩm trong 1 đơn hàng.
    public List<OrderDetail> getOrderDetailsByOrderID(int orderID) {
        List<OrderDetail> list = new ArrayList<>();

        // Sử dụng JOIN để lấy ProductName, Color, Size từ bảng Products và ProductVariants
        String sql = """
            SELECT 
                od.OrderID, 
                od.VariantID, 
                od.Quantity, 
                od.UnitPrice,
                p.ProductName, 
                pv.Color, 
                pv.Size
            FROM OrderDetails od
            JOIN ProductVariants pv ON od.VariantID = pv.VariantID
            JOIN Products p ON pv.ProductID = p.ProductID
            WHERE od.OrderID = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Sử dụng Constructor hoặc Setter để gán dữ liệu
                OrderDetail od = new OrderDetail();
                od.setOrderID(rs.getInt("OrderID"));
                od.setVariantID(rs.getInt("VariantID"));
                od.setQuantity(rs.getInt("Quantity"));
                od.setUnitPrice(rs.getDouble("UnitPrice"));
                
                // Gán thêm thông tin sản phẩm từ kết quả JOIN
                od.setProductName(rs.getString("ProductName"));
                od.setColor(rs.getString("Color"));
                od.setSize(rs.getString("Size"));
                
                list.add(od);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy OrderDetail với thông tin sản phẩm", e);
        }
        return list;
    }

//    Thêm chi tiết sản phẩm vào đơn hàng.
    public void insertOrderDetail(int orderID, int variantID, int quantity, double unitPrice) throws SQLException {
        String sql = "INSERT INTO OrderDetails (OrderID, VariantID, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ps.setInt(2, variantID);
            ps.setInt(3, quantity);
            ps.setDouble(4, unitPrice);
            ps.executeUpdate();
        }
    }

//    Xóa tất cả sản phẩm trong đơn hàng.
//    public void deleteByOrderID(int orderID) throws SQLException {
//        String sql = "DELETE FROM OrderDetails WHERE OrderID = ?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, orderID);
//            ps.executeUpdate();
//        }
//    }

//    Xóa tất cả orderDetails chứa variant này.
    public void deleteOrderDetailsByVariantID(int variantID) throws SQLException {
        String sql = "DELETE FROM OrderDetails WHERE VariantID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, variantID);
            ps.executeUpdate();
        }
    }
}