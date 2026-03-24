package model;

import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO extends DBContext {

    // ================= LOGIN =================
    public User userLogin(String username, String password) {

        String sql = "SELECT UserID, Username, Password, FullName, Role "
                + "FROM Users WHERE Username = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Không tìm thấy username");
                return null;
            }

            String dbPass = rs.getString("Password");

            System.out.println("DB Pass: [" + dbPass + "]");
            System.out.println("User nhập: [" + password + "]");

            if (!dbPass.trim().equals(password.trim())) {
                System.out.println("Password không khớp");
                return null;
            }

            User u = new User();
            u.setUserID(rs.getInt("UserID"));
            u.setUsername(rs.getString("Username"));
            u.setFullName(rs.getString("FullName"));
            u.setRole(rs.getString("Role"));

            return u;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================= REGISTER =================
    public boolean register(String username, String password, String fullName, String email) {

        if (isUsernameExist(username)) {
            return false;
        }

        String sql = "INSERT INTO Users (Username, Password, FullName, Email, Role) "
                + "VALUES (?, ?, ?, ?, 'CUSTOMER')";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ================= CHECK USERNAME =================
    public boolean isUsernameExist(String username) {
        String sql = "SELECT UserID FROM Users WHERE Username = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean changePassword(String username, String oldPass, String newPass) {

    String sqlCheck = "SELECT Password FROM Users WHERE Username = ?";
    String sqlUpdate = "UPDATE Users SET Password = ? WHERE Username = ?";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sqlCheck);
         PreparedStatement ps2 = conn.prepareStatement(sqlUpdate)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return false;
        }

        String dbPass = rs.getString("Password");

        if (!dbPass.trim().equals(oldPass.trim())) {
            return false;
        }

        ps2.setString(1, newPass);
        ps2.setString(2, username);

        return ps2.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

    public boolean insertEmployee(String username,
            String password,
            String fullName,
            String email) {

        String sql = """
        INSERT INTO Users
        (Username, Password, FullName, Email, Role)
        VALUES (?, ?, ?, ?, 'EMPLOYEE')
    """;

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean resetPassword(String username, String newPass, String confirmPass) {

        // Kiểm tra confirm
        if (!newPass.equals(confirmPass)) {
            System.out.println("Mật khẩu xác nhận không khớp!");
            return false;
        }

        String sql = "UPDATE Users SET Password = ? WHERE Username = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPass);      // Password
            ps.setString(2, username);     // Username

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
//Lấy password từ database theo username.
public String getPasswordByUsername(String username) {

    String sql = "SELECT Password FROM Users WHERE Username = ?";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("Password");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

    public User getUserByID(int userID) {
        String sql = "SELECT UserID, Username, Password, FullName, Email, Role "
                + "FROM Users WHERE UserID = ?";
        try (Connection conn = getConnection(); java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setRole(rs.getString("Role"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        // ⚠ Sửa lại cho đúng dữ liệu trong database của bạn
        String username = "customer1";   // username tồn tại trong DB
        String oldPassword = "000000";   // mật khẩu hiện tại
        String newPassword = "654321";   // mật khẩu muốn đổi

        System.out.println("===== TEST CHANGE PASSWORD =====");

        boolean result = dao.changePassword(username, oldPassword, newPassword);

        if (result) {
            System.out.println("✅ Đổi mật khẩu thành công!");
        } else {
            System.out.println("❌ Đổi mật khẩu thất bại!");
        }

        // 🔎 Kiểm tra lại mật khẩu sau khi đổi
        String updatedPass = dao.getPasswordByUsername(username);
        System.out.println("Password hiện tại trong DB: " + updatedPass);
    }
}
