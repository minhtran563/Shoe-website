package controller;

import entity.Review;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import model.ReviewDAO;

@WebServlet(name = "ReviewController", urlPatterns = {"/review"})
public class ReviewController extends HttpServlet {

    // =========================
    // GET: VIEW REVIEW LIST
    // =========================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        // kiểm tra login
        if (acc == null) {

            session.setAttribute("error", "Bạn cần đăng nhập.");
            response.sendRedirect("login");
            return;
        }

        // kiểm tra admin
        if (!"ADMIN".equalsIgnoreCase(acc.getRole())) {

            session.setAttribute("error", "Bạn không có quyền truy cập.");
            response.sendRedirect("home");
            return;
        }

        ReviewDAO dao = new ReviewDAO();

        try {

            List<Review> list = dao.getAllReviews();

            request.setAttribute("reviews", list);
            request.setAttribute("user", acc);

            request.getRequestDispatcher("view-reviews.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // POST: ADD / UPDATE / DELETE
    // =========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {

            session.setAttribute("error",
                    "Vui lòng đăng nhập để thực hiện chức năng này.");

            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");

        ReviewDAO dao = new ReviewDAO();

        int userID = acc.getUserID();

        try {

            // =========================
            // ADD REVIEW
            // =========================
            if ("add".equals(action)) {

                int productID = Integer.parseInt(request.getParameter("productID"));
                int rating = Integer.parseInt(request.getParameter("rating"));
                String comment = request.getParameter("comment");

                if (!dao.hasUserPurchasedProduct(userID, productID)) {

                    session.setAttribute("error",
                            "Bạn phải mua sản phẩm trước khi đánh giá.");

                    response.sendRedirect("detail?pid=" + productID);
                    return;
                }

//                if (dao.hasUserReviewed(userID, productID)) {
//
//                    session.setAttribute("error",
//                            "Bạn đã đánh giá sản phẩm này rồi.");
//
//                    response.sendRedirect("detail?pid=" + productID);
//                    return;
//                }

                Review r = new Review();

                r.setProductID(productID);
                r.setUserID(userID);
                r.setRating(rating);
                r.setComment(comment);

                dao.insertReview(r);

                session.setAttribute("success",
                        "Đánh giá đã được gửi.");

                response.sendRedirect("detail?pid=" + productID);
            }

            // =========================
            // UPDATE REVIEW
            // =========================
//            else if ("update".equals(action)) {
//
//                int reviewID = Integer.parseInt(request.getParameter("reviewID"));
//                int rating = Integer.parseInt(request.getParameter("rating"));
//                String comment = request.getParameter("comment");
//                int productID = Integer.parseInt(request.getParameter("productID"));
//
//                Review review = dao.getReviewByID(reviewID);
//
//                if (review != null && review.getUserID() == userID) {
//
//                    review.setRating(rating);
//                    review.setComment(comment);
//
//                    dao.updateReview(review);
//
//                    session.setAttribute("success",
//                            "Cập nhật đánh giá thành công.");
//                }
//
//                response.sendRedirect("detail?pid=" + productID);
//            }

            // =========================
            // DELETE REVIEW
            // =========================
            else if ("delete".equals(action)) {

                int reviewID = Integer.parseInt(request.getParameter("reviewID"));
                String redirectTo = request.getParameter("redirectTo");

                Review review = dao.getReviewByID(reviewID);

                if (review != null &&
                        (review.getUserID() == userID ||
                         "ADMIN".equalsIgnoreCase(acc.getRole()))) {

                    dao.deleteReview(reviewID);

                    session.setAttribute("success",
                            "Xóa đánh giá thành công.");
                }

                // Redirect về trang detail nếu từ customer, về review nếu từ admin
                if ("detail".equals(redirectTo)) {
                    int productID = Integer.parseInt(request.getParameter("productID"));
                    response.sendRedirect("detail?pid=" + productID);
                } else {
                    response.sendRedirect("review");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}