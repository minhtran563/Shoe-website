/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.UserDAO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rePassword = request.getParameter("rePassword");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");

        String error = null;

        // Trim tránh khoảng trắng
        username = username != null ? username.trim() : "";
        password = password != null ? password.trim() : "";
        rePassword = rePassword != null ? rePassword.trim() : "";
        fullName = fullName != null ? fullName.trim() : "";
        email = email != null ? email.trim() : "";

        // ===== VALIDATION =====
        if (username.length() < 6 || username.length() > 20) {
            error = "Username phải từ 6 - 20 ký tự!";
        } else if (password.length() < 6 || password.length() > 30) {
            error = "Password phải từ 6 - 30 ký tự!";
        } else if (!password.equals(rePassword)) {
            error = "Mật khẩu xác nhận không khớp!";
        } else if (fullName.length() < 6 || fullName.length() > 50) {
            error = "Full Name phải từ 6 - 50 ký tự!";
        } else if (email.isEmpty()) {
            error = "Email không được để trống!";
        } else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            error = "Email không đúng định dạng!";
        }

        // Nếu có lỗi → quay lại trang register
        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
            return;
        }

        // ===== INSERT DATABASE =====
        UserDAO dao = new UserDAO();
        boolean success = dao.register(username, password, fullName, email);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Username đã tồn tại!");
            request.getRequestDispatcher("/jsp/auth/register.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
