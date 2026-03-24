package model;

import entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO extends DBContext {

    private final Connection conn;

    // ===== CONSTRUCTOR =====
    public CartDAO() {
        this.conn = super.getConnection();
    }

    // Dùng cho TRANSACTION
    public CartDAO(Connection conn) {
        this.conn = conn;
    }

    // =====================================================
    // Lấy giỏ hàng đang ACTIVE của user.
    // =====================================================
    public Cart getActiveCart(int userID) {

        String sql = "SELECT TOP 1 * FROM Carts WHERE UserID = ? AND Status = 'ACTIVE'";

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartID(rs.getInt("CartID"));

                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                cart.setUsers(u);

                cart.setStatus(rs.getString("Status"));
                cart.setCreatedAt(rs.getTimestamp("CreatedAt"));

                return cart;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // Tạo giỏ hàng mới
    // =====================================================
    public Cart createCart(int userID) {

        Cart existing = getActiveCart(userID);
        if (existing != null) {
            return existing;
        }

        String sql = "INSERT INTO Carts (UserID, Status) VALUES (?, 'ACTIVE')";

        try (PreparedStatement st
                = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, userID);
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartID(rs.getInt(1));
                cart.setStatus("ACTIVE");
                return cart;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
//    public void deleteExpiredCartItems() {
//
//    // Tự động xóa sản phẩm trong giỏ hàng sau 24h.
//
//    String sql = "DELETE FROM dbo.CartItems WHERE CreatedAt < DATEADD(hour, -24, GETDATE())";
//
//    
//
//    try (Connection conn = new DBContext().getConnection(); 
//
//         PreparedStatement ps = conn.prepareStatement(sql)) {
//
//        
//
//        int affectedRows = ps.executeUpdate();
//
//        if (affectedRows > 0) {
//
//            System.out.println("Đã tự động xóa " + affectedRows + " sản phẩm quá hạn.");
//
//        }
//
//    } catch (Exception e) {
//
//        e.printStackTrace();
//
//    }
//    }

    // =====================================================
    // Thêm sản phẩm vào giỏ hàng.
    // =====================================================
    public boolean addToCart(int cartID, int variantID, int quantity) {

        try {

            // 🔥 CHECK STOCK
            String stockSql = "SELECT Stock FROM ProductVariants WHERE VariantID = ?";
            PreparedStatement stockSt = conn.prepareStatement(stockSql);
            stockSt.setInt(1, variantID);
            ResultSet rsStock = stockSt.executeQuery();

            if (!rsStock.next()) {
                return false;
            }

            int stock = rsStock.getInt("Stock");

            // Check tồn tại item chưa
            String checkSql = "SELECT Quantity FROM CartItems WHERE CartID = ? AND VariantID = ?";
            PreparedStatement check = conn.prepareStatement(checkSql);
            check.setInt(1, cartID);
            check.setInt(2, variantID);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {

                int currentQty = rs.getInt("Quantity");
                if (currentQty + quantity > stock) {
                    return false;
                }

                String updateSql = "UPDATE CartItems SET Quantity = Quantity + ? WHERE CartID = ? AND VariantID = ?";
                PreparedStatement update = conn.prepareStatement(updateSql);
                update.setInt(1, quantity);
                update.setInt(2, cartID);
                update.setInt(3, variantID);
                update.executeUpdate();

            } else {

                if (quantity > stock) {
                    return false;
                }

                String insertSql = "INSERT INTO CartItems (CartID, VariantID, Quantity) VALUES (?, ?, ?)";
                PreparedStatement insert = conn.prepareStatement(insertSql);
                insert.setInt(1, cartID);
                insert.setInt(2, variantID);
                insert.setInt(3, quantity);
                insert.executeUpdate();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =====================================================
    // Lấy danh sách sản phẩm trong giỏ hàng
    // =====================================================
    public List<Item> getCartItems(int cartID) {

        List<Item> list = new ArrayList<>();

        String sql = """
            SELECT ci.Quantity,
                   pv.VariantID,
                   pv.ProductID,
                   pv.Price,
                   pv.Size,
                   pv.Color,
                   pv.Stock,
                   p.ProductName,
                   p.Image
            FROM CartItems ci
            JOIN ProductVariants pv ON ci.VariantID = pv.VariantID
            JOIN Products p ON pv.ProductID = p.ProductID
            WHERE ci.CartID = ?
        """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, cartID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {

                Product product = new Product();
                product.setProductID(rs.getInt("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                product.setImage(rs.getString("Image"));

                ProductVariant variant = new ProductVariant();
                variant.setVariantID(rs.getInt("VariantID"));
                variant.setProductID(rs.getInt("ProductID"));
                variant.setPrice(rs.getDouble("Price"));
                variant.setSize(rs.getString("Size"));
                variant.setColor(rs.getString("Color"));
                variant.setStock(rs.getInt("Stock"));

                Item item = new Item();
                item.setProduct(product);
                item.setVariant(variant);
                item.setQuantity(rs.getInt("Quantity"));
                item.setPrice(rs.getDouble("Price"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // Cập nhật số lượng sản phẩm trong giỏ.
    // =====================================================
    public void updateQuantity(int cartID, int variantID, int quantity) {

        String sql = "UPDATE CartItems SET Quantity = ? WHERE CartID = ? AND VariantID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, quantity);
            st.setInt(2, cartID);
            st.setInt(3, variantID);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // Xóa 1 sản phẩm khỏi cart.
    // =====================================================
    public void removeItem(int cartID, int variantID) {

        String sql = "DELETE FROM CartItems WHERE CartID = ? AND VariantID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, cartID);
            st.setInt(2, variantID);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // Xóa toàn bộ sản phẩm trong cart.
    // =====================================================
//    public void clearCart(int cartID) {
//
//        String sql = "DELETE FROM CartItems WHERE CartID = ?";
//
//        try (PreparedStatement st = conn.prepareStatement(sql)) {
//            st.setInt(1, cartID);
//            st.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    // =====================================================
    // Cập nhật trạng thái cart.
    // =====================================================
    public void updateStatus(int cartID, String status) {

        String sql = "UPDATE Carts SET Status = ? WHERE CartID = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, cartID);
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // Tính tổng tiền giỏ hàng.
    // =====================================================
    public double getCartTotal(int cartID) {

        String sql = """
            SELECT SUM(ci.Quantity * pv.Price) AS Total
            FROM CartItems ci
            JOIN ProductVariants pv ON ci.VariantID = pv.VariantID
            WHERE ci.CartID = ?
        """;

        try (PreparedStatement st = conn.prepareStatement(sql)) {

            st.setInt(1, cartID);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                return rs.getDouble("Total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void main(String[] args) {

        CartDAO dao = new CartDAO();

        int userID = 3;        // ⚠ đổi theo user có trong DB
        int variantID = 2237;  // ⚠ đổi theo variant có trong DB

        System.out.println("===== TEST START =====");

        // =========================
        // 1️⃣ GET OR CREATE CART
        // =========================
        Cart cart = dao.getActiveCart(userID);

        if (cart == null) {
            System.out.println("Không có ACTIVE cart → tạo mới");
            cart = dao.createCart(userID);
        }

        System.out.println("CartID ACTIVE: " + cart.getCartID());

        // =========================
        // 2️⃣ TEST ADD TO CART
        // =========================
        System.out.println("\n===== ADD TO CART =====");

        boolean added = dao.addToCart(cart.getCartID(), variantID, 2);

        if (added) {
            System.out.println("Thêm sản phẩm thành công");
        } else {
            System.out.println("Thêm sản phẩm thất bại (vượt stock?)");
        }

        // =========================
        // 3️⃣ TEST GET ITEMS
        // =========================
        System.out.println("\n===== GET CART ITEMS =====");

        List<Item> items = dao.getCartItems(cart.getCartID());

        for (Item item : items) {
            System.out.println("Product: " + item.getProduct().getProductName());
            System.out.println("Quantity: " + item.getQuantity());
            System.out.println("Stock: " + item.getVariant().getStock());
            System.out.println("Subtotal: " + item.getPrice() * item.getQuantity());
            System.out.println("---------------------------");
        }

        // =========================
        // 4️⃣ TEST UPDATE QUANTITY
        // =========================
        System.out.println("\n===== UPDATE QUANTITY =====");

        dao.updateQuantity(cart.getCartID(), variantID, 5);

        items = dao.getCartItems(cart.getCartID());
        System.out.println("Quantity sau update: " + items.get(0).getQuantity());

        // =========================
        // 5️⃣ TEST TOTAL
        // =========================
        System.out.println("\n===== TEST TOTAL =====");

        double total = dao.getCartTotal(cart.getCartID());
        System.out.println("Tổng tiền: " + total);

        // =========================
        // 6️⃣ TEST CHECKOUT
        // =========================
        System.out.println("\n===== TEST CHECKOUT =====");

        dao.updateStatus(cart.getCartID(), "CHECKED_OUT");
        System.out.println("Cart đã chuyển sang CHECKED_OUT");

        // =========================
        // 7️⃣ CREATE NEW ACTIVE CART
        // =========================
        System.out.println("\n===== CREATE NEW ACTIVE CART =====");

        Cart newCart = dao.createCart(userID);
        System.out.println("Cart mới ACTIVE: " + newCart.getCartID());

        // =========================
        // 8️⃣ VERIFY NO DOUBLE ACTIVE
        // =========================
        System.out.println("\n===== VERIFY SINGLE ACTIVE =====");

        Cart verify = dao.getActiveCart(userID);
        System.out.println("Cart ACTIVE hiện tại: " + verify.getCartID());

        System.out.println("\n===== TEST END =====");
    }
}
