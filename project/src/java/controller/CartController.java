package controller;

import entity.Cart;
import entity.Item;
import entity.ProductVariant;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.CartDAO;
import model.CategoryDAO;
import model.ProductVariantDAO;

@WebServlet(name = "CartController", urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

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
//        cartDAO.deleteExpiredCartItems();
        ProductVariantDAO variantDAO = new ProductVariantDAO();

        int userID = user.getUserID();

        // ===== LẤY HOẶC TẠO CART =====
        Cart cart = cartDAO.getActiveCart(userID);
        if (cart == null) {
            cartDAO.createCart(userID);
            cart = cartDAO.getActiveCart(userID);
        }

        String action = request.getParameter("action");
        String pidStr = request.getParameter("pid");

        if (pidStr != null && !pidStr.isEmpty()) {
            try {
                int variantID = Integer.parseInt(pidStr);
                ProductVariant variant = variantDAO.getVariantByID(variantID);

                if (variant != null && variant.getStock() > 0) {

                    if ("add".equals(action) || "buy".equals(action)) {
                        cartDAO.addToCart(cart.getCartID(), variantID, 1);

                        if ("buy".equals(action)) {
                            response.sendRedirect("home");
                            return;
                        }
                    } else if ("minus".equals(action)) {

                        List<Item> items = cartDAO.getCartItems(cart.getCartID());

                        for (Item item : items) {
                            if (item.getVariant().getVariantID() == variantID) {

                                if (item.getQuantity() > 1) {
                                    cartDAO.updateQuantity(
                                            cart.getCartID(),
                                            variantID,
                                            item.getQuantity() - 1
                                    );
                                } else {
                                    cartDAO.removeItem(cart.getCartID(), variantID);
                                }
                                break;
                            }
                        }
                    } else if ("remove".equals(action)) {
                        cartDAO.removeItem(cart.getCartID(), variantID);
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // ===== LOAD CART ITEMS =====
        List<Item> items = cartDAO.getCartItems(cart.getCartID());
        session.setAttribute("cartSize", items.size());

        request.setAttribute("cartItems", items);
        request.setAttribute("categories", new CategoryDAO().getAll());

        request.getRequestDispatcher("cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
