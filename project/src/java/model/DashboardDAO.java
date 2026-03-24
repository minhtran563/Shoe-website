package model;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO extends DBContext {

//    Lấy tổng số sản phẩm đang bán (ACTIVE).
    public int getTotalProducts() {

        String sql = "SELECT COUNT(*) FROM Products WHERE Status = 'ACTIVE'";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

//    Tính tổng số sản phẩm đã bán.
    public int getSoldProducts() {

        String sql = """
            SELECT ISNULL(SUM(od.Quantity), 0)
            FROM OrderDetails od
            JOIN Orders o ON od.OrderID = o.OrderID
            WHERE o.Status = 'Completed'
            AND o.PaymentStatus = 'PAID'
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Tính tổng doanh thu hệ thống.
    public double getTotalRevenue() {

        String sql = """
            SELECT ISNULL(SUM(TotalPrice), 0)
            FROM Orders
            WHERE Status = 'Completed'
            AND PaymentStatus = 'PAID'
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //Lấy danh sách khách hàng.
    public List<User> getCustomerList() {

        List<User> list = new ArrayList<>();

        String sql = """
            SELECT UserID, Username, FullName, Email
            FROM Users
            WHERE Role = 'CUSTOMER'
            ORDER BY UserID DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy danh sách nhân viên.
    public List<User> getEmployeeList() {

        List<User> list = new ArrayList<>();

        String sql = """
            SELECT UserID, Username, FullName, Email
            FROM Users
            WHERE Role = 'EMPLOYEE'
            ORDER BY UserID DESC
        """;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public static void main(String[] args) {

    DashboardDAO dao = new DashboardDAO();

    System.out.println("===== TEST DASHBOARD =====");

    // 1️⃣ Tổng sản phẩm
    int totalProducts = dao.getTotalProducts();
    System.out.println("Tổng sản phẩm: " + totalProducts);

    // 2️⃣ Sản phẩm đã bán
    int soldProducts = dao.getSoldProducts();
    System.out.println("Sản phẩm đã bán: " + soldProducts);

    // 3️⃣ Tổng doanh thu
    double totalRevenue = dao.getTotalRevenue();
    System.out.println("Tổng doanh thu: " + totalRevenue);

    // 4️⃣ Danh sách khách hàng
    System.out.println("\n--- Danh sách khách hàng ---");
    dao.getCustomerList().forEach(u -> {
        System.out.println(
                "ID: " + u.getUserID()
                + " | Username: " + u.getUsername()
                + " | Name: " + u.getFullName()
                + " | Email: " + u.getEmail()
        );
    });

    // 5️⃣ Danh sách nhân viên
    System.out.println("\n--- Danh sách nhân viên ---");
    dao.getEmployeeList().forEach(e -> {
        System.out.println(
                "ID: " + e.getUserID()
                + " | Username: " + e.getUsername()
                + " | Name: " + e.getFullName()
                + " | Email: " + e.getEmail()
        );
    });

    System.out.println("===== END TEST =====");
}
}