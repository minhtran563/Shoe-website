
package controller;

import entity.Cart;
import entity.Item;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import model.CartDAO;
import model.DBContext;
import model.OrderDAO;
import model.OrderDetailDAO;
import model.ProductVariantDAO;

@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    /* ================== GET ================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        Cart cart = cartDAO.getActiveCart(user.getUserID());

        if (cart == null) {
            response.sendRedirect("cart");
            return;
        }

        List<Item> items = cartDAO.getCartItems(cart.getCartID());

        if (items == null || items.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        request.setAttribute("cartItems", items);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    /* ================== POST ================== */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        CartDAO cartDAO = new CartDAO();
        Cart cart = cartDAO.getActiveCart(user.getUserID());

        if (cart == null) {
            response.sendRedirect("cart");
            return;
        }

        List<Item> items = cartDAO.getCartItems(cart.getCartID());

        if (items == null || items.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        /* ===== LẤY DATA TỪ FORM ===== */
        String paymentMethod = request.getParameter("paymentMethod");
        String shippingAddress = request.getParameter("shippingAddress");
        String phoneNumber = request.getParameter("phoneNumber");

        /* ===== TÍNH TOTAL SERVER ===== */
        double totalPrice = 0;
        for (Item item : items) {
            totalPrice += item.getPrice() * item.getQuantity();
        }

        Connection conn = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // START TRANSACTION

            OrderDAO orderDAO = new OrderDAO(conn);
            OrderDetailDAO detailDAO = new OrderDetailDAO(conn);
            ProductVariantDAO variantDAO = new ProductVariantDAO(conn);

            /* ===== CHECK STOCK ===== */
            for (Item item : items) {
                if (item.getVariant() == null) {
    throw new RuntimeException("Variant NULL");
}

                int variantID = item.getVariant().getVariantID();
                int qty = item.getQuantity();

                if (!variantDAO.isEnoughStock(variantID, qty)) {

                    conn.rollback();

                    request.setAttribute(
                            "error",
                            "Sản phẩm \""
                            + item.getProduct().getProductName()
                            + "\" không đủ số lượng trong kho!"
                    );

                    request.getRequestDispatcher("cart")
                            .forward(request, response);
                    return;
                }
            }

            /* ===== XÁC ĐỊNH STATUS ===== */
            String paymentStatus;
            String orderStatus;

            if ("COD".equalsIgnoreCase(paymentMethod)) {
                paymentStatus = "UNPAID";
                orderStatus = "Pending";
            } else {
                paymentStatus = "PAID";
                orderStatus = "Completed";
            }

            /* ===== INSERT ORDER ===== */
            int orderID = orderDAO.insertOrder(
                    user.getUserID(),
                    totalPrice,
                    paymentMethod,
                    paymentStatus,
                    orderStatus,
                    shippingAddress,
                    phoneNumber
            );

            /* ===== INSERT ORDER DETAIL + TRỪ KHO ===== */
            for (Item item : items) {

                int variantID = item.getVariant().getVariantID();
                int qty = item.getQuantity();

                detailDAO.insertOrderDetail(
                        orderID,
                        variantID,
                        qty,
                        item.getPrice()
                );

                variantDAO.decreaseStock(variantID, qty);
            }

            /* ===== CLEAR CART ===== */
            cartDAO.updateStatus(cart.getCartID(), "CHECKED_OUT");
            cartDAO.createCart(user.getUserID());

            conn.commit(); // COMMIT

            session.setAttribute("cartSize", 0);

            response.sendRedirect("thankyou.jsp");

        } catch (Exception e) {

            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
request.getRequestDispatcher("checkout.jsp").forward(request, response);

        } finally {

            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Checkout Controller";
    }
}