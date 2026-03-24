package controller;

import entity.Product;
import entity.ProductVariant;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import model.CategoryDAO;
import model.DashboardDAO;
import model.ProductDAO;
import model.ProductVariantDAO;
import model.ReviewDAO;
import model.OrderDetailDAO;
import model.UserDAO;

@WebServlet(
        name = "ManagerController",
        urlPatterns = {
            "/manager",
            "/add-product",
            "/edit-product",
            "/delete-product",
            "/add-variant",
            "/edit-variant",
            "/delete-variant",
            "/add-employee",
            "/view-reviews"// ✅ thêm vào đây
        }
)
public class ManagerController extends HttpServlet {

    /* ========================== GET ========================== */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        ProductDAO pDao = new ProductDAO();
        CategoryDAO cDao = new CategoryDAO();
        ProductVariantDAO vDao = new ProductVariantDAO();
        ReviewDAO rDao = new ReviewDAO();
        OrderDetailDAO odDao = new OrderDetailDAO();

        switch (path) {

            case "/manager": {

                String txtSearch = request.getParameter("txtSearch");

                if (txtSearch == null || txtSearch.trim().isEmpty()) {
                    request.setAttribute("data", pDao.getAllProductsAdmin());
                } else {
                    request.setAttribute("data", pDao.search(txtSearch.trim()));
                }

                request.setAttribute("categories", cDao.getAll());
                request.getRequestDispatcher("productmanager.jsp")
                        .forward(request, response);
                break;
            }

            case "/add-employee": {

                User acc = (User) request.getSession().getAttribute("acc");

                if (acc == null || !"ADMIN".equals(acc.getRole())) {
                    response.sendRedirect("login");
                    return;
                }

                request.getRequestDispatcher("addemployee.jsp")
                        .forward(request, response);
                break;
            }

            case "/edit-product": {

                String pidRaw = request.getParameter("pid");
                if (pidRaw == null || pidRaw.isEmpty()) {
                    response.sendRedirect("manager");
                    return;
                }

                int pid = Integer.parseInt(pidRaw);

                request.setAttribute("st", pDao.getProductByID(pid));
                request.setAttribute("categories", cDao.getAll());
                request.setAttribute("variants",
                        vDao.getVariantsByProductID(pid));

                request.getRequestDispatcher("editproduct.jsp")
                        .forward(request, response);
                break;
            }

            case "/delete-product": {

                String pidRaw = request.getParameter("pid");
                if (pidRaw == null || pidRaw.isEmpty()) {
                    response.sendRedirect("manager");
                    return;
                }

                int pid = Integer.parseInt(pidRaw);

                try {

                    rDao.deleteReviewsByProductID(pid);

                    List<ProductVariant> variants
                            = vDao.getVariantsByProductID(pid);

                    for (ProductVariant v : variants) {
                        odDao.deleteOrderDetailsByVariantID(
                                v.getVariantID());
                    }

                    vDao.deleteVariantsByProductID(pid);

                    boolean success = pDao.deleteProduct(pid);

                    response.sendRedirect(
                            success
                                    ? "manager?msg=delete_success"
                                    : "manager?msg=delete_fail"
                    );

                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("manager?msg=delete_fail");
                }

                break;
            }

            case "/delete-variant": {

                String vidRaw = request.getParameter("variantID");
                String pidRaw = request.getParameter("productID");

                if (vidRaw == null || pidRaw == null) {
                    response.sendRedirect("manager");
                    return;
                }

                int variantID = Integer.parseInt(vidRaw);
                int productID = Integer.parseInt(pidRaw);

                try {
                    odDao.deleteOrderDetailsByVariantID(variantID);
                    vDao.deleteVariant(variantID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect("edit-product?pid=" + productID);
                break;
            }

            case "/view-reviews": {

                String pidRaw = request.getParameter("pid");

                if (pidRaw != null && !pidRaw.isEmpty()) {

                    int pid = Integer.parseInt(pidRaw);
                    request.setAttribute("reviews", rDao.getReviewsByProductID(pid));

                } else {

                    // nếu không có pid -> lấy tất cả review
                    request.setAttribute("reviews", rDao.getAllReviews());

                }

                request.getRequestDispatcher("view-reviews.jsp")
                        .forward(request, response);
                break;
            }

        }
    }

    /* ========================== POST ========================== */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        ProductDAO pDao = new ProductDAO();
        ProductVariantDAO vDao = new ProductVariantDAO();

        /* ================= ADD PRODUCT ================= */
        if (path.equals("/add-product")) {

            try {
                Product p = new Product(
                        0,
                        request.getParameter("name"),
                        Integer.parseInt(request.getParameter("category")),
                        request.getParameter("description"),
                        Double.parseDouble(request.getParameter("price")),
                        request.getParameter("image"),
                        request.getParameter("status")
                );

                boolean success = pDao.insertProduct(p);

                response.sendRedirect(
                        success
                                ? "manager?msg=add_success"
                                : "manager?msg=add_fail"
                );

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manager?msg=add_fail");
            }
            return;
        }

        /* ================= EDIT PRODUCT ================= */
        if (path.equals("/edit-product")) {

            try {
                int productID = Integer.parseInt(request.getParameter("productID"));

                Product p = new Product(
                        productID,
                        request.getParameter("name"),
                        Integer.parseInt(request.getParameter("category")),
                        request.getParameter("description"),
                        Double.parseDouble(request.getParameter("price")),
                        request.getParameter("image"),
                        request.getParameter("status")
                );

                boolean success = pDao.updateProduct(p);

                response.sendRedirect(
                        success
                                ? "manager?msg=update_success"
                                : "manager?msg=update_fail"
                );

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manager?msg=update_fail");
            }
            return;
        }

        /* ================= ADD VARIANT ================= */
        if (path.equals("/add-variant")) {

            try {

                int productID = Integer.parseInt(request.getParameter("productID"));
                String size = request.getParameter("size");
                String color = request.getParameter("color");
                double price = Double.parseDouble(request.getParameter("price"));
                int stock = Integer.parseInt(request.getParameter("stock"));

                ProductVariant v = new ProductVariant(
                        0,
                        productID,
                        size,
                        color,
                        price,
                        stock
                );

                vDao.insertVariant(v);

                response.sendRedirect("edit-product?pid=" + productID);
                return;

            } catch (Exception e) {

                e.printStackTrace();
                response.sendRedirect("manager");
                return;

            }
        }

        /* ================= EDIT VARIANT ================= */
        if (path.equals("/edit-variant")) {

            try {
                int productID = Integer.parseInt(request.getParameter("productID"));

                ProductVariant v = new ProductVariant(
                        Integer.parseInt(request.getParameter("variantID")),
                        productID,
                        request.getParameter("size"),
                        request.getParameter("color"),
                        Double.parseDouble(request.getParameter("price")),
                        Integer.parseInt(request.getParameter("stock"))
                );

                vDao.updateVariant(v);
                response.sendRedirect("edit-product?pid=" + productID);

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manager");
            }
            return;
        }

        /* ================= ADD EMPLOYEE ================= */
        if (path.equals("/add-employee")) {

            User acc = (User) request.getSession().getAttribute("acc");

            if (acc == null || !"ADMIN".equals(acc.getRole())) {
                response.sendRedirect("login");
                return;
            }

            try {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");

                // ===== TRIM DATA =====
                username = username != null ? username.trim() : "";
                password = password != null ? password.trim() : "";
                fullName = fullName != null ? fullName.trim() : "";
                email = email != null ? email.trim() : "";

                String error = null;

                // ===== VALIDATION =====
                if (username.length() < 6 || username.length() > 20) {
                    error = "Username phải từ 6 - 20 ký tự!";
                } else if (password.length() < 6 || password.length() > 30) {
                    error = "Password phải từ 6 - 30 ký tự!";
                } else if (fullName.length() < 6 || fullName.length() > 50) {
                    error = "Full Name phải từ 6 - 50 ký tự!";
                } else if (email.isEmpty()) {
                    error = "Email không được để trống!";
                } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
                    error = "Email không đúng định dạng!";
                }

                if (error != null) {
                    request.setAttribute("error", error);
                    request.getRequestDispatcher("addemployee.jsp")
                            .forward(request, response);
                    return;
                }

                UserDAO uDao = new UserDAO();

                // Check username tồn tại
                if (uDao.isUsernameExist(username)) {
                    request.setAttribute("error", "Username đã tồn tại!");
                    request.getRequestDispatcher("addemployee.jsp")
                            .forward(request, response);
                    return;
                }

                // Nếu bạn có hàm check email tồn tại thì thêm:
                /*
        if (uDao.isEmailExist(email)) {
            request.setAttribute("error", "Email đã tồn tại!");
            request.getRequestDispatcher("addemployee.jsp")
                    .forward(request, response);
            return;
        }
                 */
                boolean success = uDao.insertEmployee(username, password, fullName, email);

                if (success) {
                    response.sendRedirect("manager?msg=employee_added");
                } else {
                    request.setAttribute("error", "Thêm nhân viên thất bại!");
                    request.getRequestDispatcher("addemployee.jsp")
                            .forward(request, response);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("manager");
            }
            return;
        }

        /* ================= DEFAULT ================= */
        response.sendRedirect("manager");
    }

    // Các phần add-product, edit-product, add-variant, edit-variant giữ nguyên như bạn đã làm
    @Override
    public String getServletInfo() {
        return "Manager Controller";
    }
}
