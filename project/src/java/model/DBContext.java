/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class DBContext {
    
    protected Connection connection;

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            // Sửa lại databaseName cho đúng với file SQL đã tạo
            String url = "jdbc:sqlserver://localhost:1433;databaseName=Sport4;encrypt=false";
            String user = "sa"; 
            String password = "123"; // Thay bằng mật khẩu máy bạn (thường là 123 hoặc 123456)

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, user, password);
            }
            return connection;
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            return null;
        }
    }

    public ResultSet getData(String sql) {
        ResultSet rs = null;
        try {
            // Đảm bảo conn đã được kết nối trước khi tạo Statement
            Connection currentConn = getConnection(); 
            if (currentConn != null) {
                Statement state = currentConn.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs = state.executeQuery(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, "Lỗi SQL: " + sql, ex);
        }
        return rs;
    }

    // Hàm main để test thử kết nối
    public static void main(String[] args) {
        DBContext db = new DBContext();
        if (db.getConnection() != null) {
            System.out.println("Chúc mừng! Kết nối SQL Server thành công.");
        } else {
            System.out.println("Kết nối thất bại. Vui lòng kiểm tra lại User/Pass hoặc tên Database.");
        }
    }
}
