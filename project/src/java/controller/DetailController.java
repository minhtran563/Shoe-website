package controller;

import entity.Product;
import entity.ProductVariant;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

import model.ProductDAO;
import model.ProductVariantDAO;
import model.ReviewDAO;

@WebServlet(name = "DetailController", urlPatterns = {"/detail"})
public class DetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pidRaw = request.getParameter("pid");

        // ===== CHECK PID =====
        if (pidRaw == null || pidRaw.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        try {

            int productId = Integer.parseInt(pidRaw);

            // ===== DAO =====
            ProductDAO productDAO = new ProductDAO();
            ProductVariantDAO variantDAO = new ProductVariantDAO();
            ReviewDAO reviewDAO = new ReviewDAO();

            // ===== GET PRODUCT =====
            Product product = productDAO.getProductByID(productId);

            if (product == null) {
                response.sendRedirect("home");
                return;
            }

            // ===== GET VARIANTS =====
            List<ProductVariant> variants =
                    variantDAO.getVariantsByProductID(productId);

            // ===== GET REVIEWS =====
            request.setAttribute("reviews",
                    reviewDAO.getReviewsByProductID(productId));

            // ===== GET AVERAGE RATING =====
            request.setAttribute("avgRating",
                    reviewDAO.getAverageRating(productId));

            // ===== CHECK CAN REVIEW =====
            boolean canReview = false;

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");

            if (user != null) {

                boolean purchased = reviewDAO.hasUserPurchasedProduct(
                        user.getUserID(), productId);

                boolean reviewed = reviewDAO.hasUserReviewed(
                        user.getUserID(), productId);

                if (purchased && !reviewed) {
                    canReview = true;
                }
            }

            request.setAttribute("canReview", canReview);

            // ===== SET DATA =====
            request.setAttribute("product", product);
            request.setAttribute("variants", variants);

            // ===== FORWARD =====
            request.getRequestDispatcher("detail.jsp")
                    .forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Product Detail Controller";
    }
}