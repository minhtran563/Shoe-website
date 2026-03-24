/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import entity.Category;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author Admin
 */
public class CategoryDAO extends DBContext {
    
 // Lấy tất cả category trong database.
    public List<Category> getAll() {
    List<Category> list = new ArrayList<>();
    String sql = "SELECT CategoryID, CategoryName FROM Categories";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            list.add(new Category(
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName")
            ));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    // Lấy 1 category theo ID.
    public Category getById(int id) {
        String sql = "SELECT CategoryID, CategoryName FROM Categories WHERE CategoryID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
    CategoryDAO dao = new CategoryDAO();
    List<Category> list = dao.getAll();

    for (Category c : list) {
        System.out.println(c.getCategoryID() + " - " + c.getCategoryName());
    }
}
}
