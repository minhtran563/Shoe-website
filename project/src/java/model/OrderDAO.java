package model;

import entity.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO extends DBContext {

    private final Connection conn;

    /* ========= CONSTRUCTOR ========= */

    // Dùng bình thường
    public OrderDAO() {
        this.conn = super.getConnection();
    }

    // Dùng cho TRANSACTION (checkout)
    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    /* ========= INSERT ORDER ========= */
//Tạo đơn hàng mới.
    public int insertOrder(int userID,
                           double totalPrice,
                           String paymentMethod,
                           String paymentStatus,
                           String status,
                           String shippingAddress,
                           String phoneNumber) throws SQLException {

        String sql = """
            INSERT INTO Orders
            (UserID, OrderDate, TotalPrice, PaymentMethod,
             PaymentStatus, Status, ShippingAddress, PhoneNumber)
            VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userID);
            ps.setDouble(2, totalPrice);
            ps.setString(3, paymentMethod);
            ps.setString(4, paymentStatus);
            ps.setString(5, status);
            ps.setString(6, shippingAddress);
            ps.setString(7, phoneNumber);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // OrderID
                }
            }
        }

        throw new SQLException("Không thể tạo Order");
    }

    /* ========= GET ORDERS BY USER ========= */
//Lấy tất cả đơn hàng của 1 user.
//    public List<Order> getOrdersByUserID(int userID) {
//        List<Order> list = new ArrayList<>();
//
//        String sql = """
//            SELECT OrderID, UserID, OrderDate, TotalPrice,
//                   PaymentMethod, PaymentStatus, Status,
//                   ShippingAddress, PhoneNumber
//            FROM Orders
//            WHERE UserID = ?
//            ORDER BY OrderID DESC
//        """;
//
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setInt(1, userID);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                while (rs.next()) {
//                    Order o = new Order(
//                            rs.getInt("OrderID"),
//                            rs.getInt("UserID"),
//                            rs.getTimestamp("OrderDate"),
//                            rs.getDouble("TotalPrice"),
//                            rs.getString("PaymentMethod"),
//                            rs.getString("PaymentStatus"),
//                            rs.getString("Status"),
//                            rs.getString("ShippingAddress"),
//                            rs.getString("PhoneNumber")
//                    );
//                    list.add(o);
//                }
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Lỗi lấy order theo userID", e);
//        }
//
//        return list;
//    }

    /* ========= GET ORDER BY ID ========= */
//Lấy 1 đơn hàng theo ID.
    public Order getOrderByID(int orderID) {

        String sql = """
            SELECT OrderID, UserID, OrderDate, TotalPrice,
                   PaymentMethod, PaymentStatus, Status,
                   ShippingAddress, PhoneNumber
            FROM Orders
            WHERE OrderID = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(
                            rs.getInt("OrderID"),
                            rs.getInt("UserID"),
                            rs.getTimestamp("OrderDate"),
                            rs.getDouble("TotalPrice"),
                            rs.getString("PaymentMethod"),
                            rs.getString("PaymentStatus"),
                            rs.getString("Status"),
                            rs.getString("ShippingAddress"),
                            rs.getString("PhoneNumber")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi lấy order theo ID", e);
        }

        return null;
    }

    /* ========= UPDATE ORDER STATUS ========= */
//Cập nhật trạng thái đơn hàng.
    public boolean updateOrderStatus(int orderID, String status) {
        String sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========= UPDATE PAYMENT STATUS ========= */
//    Cập nhật trạng thái thanh toán.
    public boolean updatePaymentStatus(int orderID, String paymentStatus) {
        String sql = "UPDATE Orders SET PaymentStatus = ? WHERE OrderID = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, paymentStatus);
            ps.setInt(2, orderID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ========= GET ALL ORDERS + CUSTOMER NAME + FILTER ========= */
//Lấy tất cả đơn hàng + tên khách hàng + filter.
    public Map<Order, String> getAllOrdersWithCustomerName(
            String fromDate, String toDate, String paymentStatus) {

        Map<Order, String> map = new LinkedHashMap<>();

        StringBuilder sql = new StringBuilder("""
            SELECT o.OrderID, o.UserID, u.FullName,
                   o.OrderDate, o.TotalPrice,
                   o.PaymentMethod, o.PaymentStatus, o.Status,
                   o.ShippingAddress, o.PhoneNumber
            FROM Orders o
            JOIN Users u ON o.UserID = u.UserID
            WHERE 1=1
        """);

        if (fromDate != null && !fromDate.isEmpty()) {
            sql.append(" AND o.OrderDate >= CAST(? AS DATE)");
        }
        if (toDate != null && !toDate.isEmpty()) {
            sql.append(" AND o.OrderDate < DATEADD(day, 1, CAST(? AS DATE))");
        }
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            sql.append(" AND o.PaymentStatus = ?");
        }

        sql.append(" ORDER BY o.OrderDate DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setString(index++, fromDate);
            }
            if (toDate != null && !toDate.isEmpty()) {
                ps.setString(index++, toDate);
            }
            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                ps.setString(index++, paymentStatus);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setUserID(rs.getInt("UserID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setTotalPrice(rs.getDouble("TotalPrice"));
                o.setPaymentMethod(rs.getString("PaymentMethod"));
                o.setPaymentStatus(rs.getString("PaymentStatus"));
                o.setStatus(rs.getString("Status"));
                o.setShippingAddress(rs.getString("ShippingAddress"));
                o.setPhoneNumber(rs.getString("PhoneNumber"));

                map.put(o, rs.getString("FullName"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }
}