/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Cart;
import entity.Item;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.CartDAO;
import model.UserDAO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

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
            out.println("<title>Servlet LoginController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginController at " + request.getContextPath() + "</h1>");
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
        // Luôn đi từ gốc webapp
        request.getRequestDispatcher("/jsp/auth/login.jsp").forward(request, response);
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String uName = request.getParameter("username");
    String pWord = request.getParameter("password");

    UserDAO dao = new UserDAO();
    User user = dao.userLogin(uName, pWord);

    if (user == null) {
        request.setAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
        request.getRequestDispatcher("/jsp/auth/login.jsp").forward(request, response);
    } else {

        HttpSession session = request.getSession();
        session.setAttribute("acc", user);

        // ===== CART DATABASE INTEGRATION =====
        CartDAO cartDAO = new CartDAO();

        // Lấy cart ACTIVE
        Cart cart = cartDAO.getActiveCart(user.getUserID());

        // Nếu chưa có → tạo mới
        if (cart == null) {
            cartDAO.createCart(user.getUserID());
            cart = cartDAO.getActiveCart(user.getUserID());
        }

        // Load cart items để hiển thị số lượng trên header
        List<Item> items = cartDAO.getCartItems(cart.getCartID());
        session.setAttribute("cartSize", items.size());

        String cp = request.getContextPath();

        switch (user.getRole()) {
            case "ADMIN":
                response.sendRedirect(cp + "/add-employee");
                break;
            case "EMPLOYEE":
                response.sendRedirect(cp + "/manager");
                break;
            default:
                response.sendRedirect(cp + "/home");
                break;
        }
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
