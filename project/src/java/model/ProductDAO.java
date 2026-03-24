package model;

import entity.Product;
import entity.ProductVariant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DBContext {

    // 1. Lấy tất cả sản phẩm đang bán (ACTIVE) cho khách hàng.
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE Status = 'ACTIVE'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy sản phẩm theo CategoryID.
    public List<Product> getProductsByCategory(int cid) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE CategoryID = ? AND Status = 'ACTIVE'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3.Lấy chi tiết 1 sản phẩm.
    public Product getProductByID(int pid) {
        String sql = "SELECT * FROM Products WHERE ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 4. Tìm kiếm sản phẩm theo tên sản phẩm.
    public List<Product> search(String txtSearch) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE ProductName LIKE ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + txtSearch + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm sản phẩm mới.
    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO Products "
                + "(ProductName, CategoryID, Description, Price, Image, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setString(5, p.getImage());
            ps.setString(6, p.getStatus()); // ⭐ QUAN TRỌNG

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//Xóa product và tất cả variants của nó.
    public boolean deleteProduct(int productID) {
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        // 1. Lấy toàn bộ variant của product
        List<ProductVariant> variants = variantDAO.getVariantsByProductID(productID);

        // 2. Xóa từng variant
        for (ProductVariant v : variants) {
            boolean deleted = variantDAO.deleteVariant(v.getVariantID());
            if (!deleted) {
                System.out.println("❌ Xóa variant thất bại: " + v.getVariantID());
                return false;
            }
        }

        // 3. Xóa product
        String sql = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Cập nhật thông tin sản phẩm.
    public boolean updateProduct(Product p) {
        String sql = "UPDATE Products SET ProductName = ?, CategoryID = ?, Description = ?, "
                + "Price = ?, Image = ?, Status = ? WHERE ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setInt(2, p.getCategoryID());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setString(5, p.getImage());
            ps.setString(6, p.getStatus());
            ps.setInt(7, p.getProductID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Lấy product từ variantID.
    public Product getProductByVariant(int vid) {
        String sql = "SELECT p.* FROM Products p JOIN ProductVariants pv ON p.ProductID = pv.ProductID WHERE pv.VariantID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//Phân trang sản phẩm.
    public List<Product> getNext6Product(int index) {
        List<Product> list = new ArrayList<>();
        // SQL Server: Bỏ qua (index-1)*6 dòng và lấy 6 dòng tiếp theo
        String sql = "SELECT * FROM Products WHERE Status = 'ACTIVE' "
                + "ORDER BY ProductID "
                + "OFFSET ? ROWS FETCH NEXT 6 ROWS ONLY";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Ví dụ: Trang 1 -> lấy từ dòng 0. Trang 2 -> lấy từ dòng 6.
            ps.setInt(1, (index - 1) * 6);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getInt("CategoryID"), rs.getString("Description"),
                        rs.getDouble("Price"), rs.getString("Image"), rs.getString("Status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
// Tính tổng số trang.
    public int getTotalPage() {
        String sql = "SELECT COUNT(*) FROM Products WHERE Status = 'ACTIVE'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int totalProducts = rs.getInt(1);
                // Công thức: Tổng trang = Làm tròn lên (Tổng sản phẩm / số sản phẩm mỗi trang)
                int endPage = totalProducts / 6;
                if (totalProducts % 6 != 0) {
                    endPage++;
                }
                return endPage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    //Lấy toàn bộ product cho ADMIN.
    public List<Product> getAllProductsAdmin() {
    List<Product> list = new ArrayList<>();

    String sql = "SELECT * FROM Products ORDER BY ProductID ASC";

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            list.add(new Product(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getInt("CategoryID"),
                rs.getString("Description"),
                rs.getDouble("Price"),
                rs.getString("Image"),
                rs.getString("Status")
            ));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
 public static void main(String[] args) {

        ProductDAO productDAO = new ProductDAO();

        int productID = 3; // 🔥 đổi ID bạn muốn test

        System.out.println("Đang xóa Product ID = " + productID);

        boolean result = productDAO.deleteProduct(productID);

        if (result) {
            System.out.println("✅ Xóa product thành công!");
        } else {
            System.out.println("❌ Xóa product thất bại!");
        }
    }
}
