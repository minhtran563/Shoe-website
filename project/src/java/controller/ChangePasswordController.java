package controller;

import entity.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.UserDAO;

@WebServlet(name = "ChangePasswordController", urlPatterns = {"/changepassword"})
public class ChangePasswordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/auth/ChangePassword.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String oldPass = request.getParameter("oldPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        String error = null;

        // ===== TRIM DATA =====
        oldPass = oldPass != null ? oldPass.trim() : "";
        newPass = newPass != null ? newPass.trim() : "";
        confirm = confirm != null ? confirm.trim() : "";

        // ===== VALIDATION =====
        if (oldPass.isEmpty()) {
            error = "Vui lòng nhập mật khẩu cũ!";
        } else if (newPass.length() < 6 || newPass.length() > 30) {
            error = "Mật khẩu mới phải từ 6 - 30 ký tự!";
        } else if (!newPass.equals(confirm)) {
            error = "Xác nhận mật khẩu không khớp!";
        } else if (newPass.equals(oldPass)) {
            error = "Mật khẩu mới không được trùng mật khẩu cũ!";
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/jsp/auth/ChangePassword.jsp")
                    .forward(request, response);
            return;
        }

        // ===== UPDATE PASSWORD =====
        UserDAO dao = new UserDAO();
        boolean success = dao.changePassword(username, oldPass, newPass);

        if (!success) {
            request.setAttribute("error", "Mật khẩu cũ không đúng!");
            request.getRequestDispatcher("/jsp/auth/ChangePassword.jsp")
                    .forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    @Override
    public String getServletInfo() {
        return "Change Password Controller";
    }
}
