/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import entity.Order;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import model.OrderDAO;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderHistory", urlPatterns = {"/orderhistory"})
public class OrderHistoryController extends HttpServlet {

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
            out.println("<title>Servlet OrderHistory</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderHistory at " + request.getContextPath() + "</h1>");
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        User acc = (User) session.getAttribute("acc");

        // ===== CHECK LOGIN + ROLE =====
        if (acc == null
                || (!"EMPLOYEE".equals(acc.getRole()) && !"ADMIN".equals(acc.getRole()))) {
            resp.sendRedirect(req.getContextPath() + "/jsp/auth/login.jsp");
            return;
        }

        // ===== LẤY FILTER =====
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String paymentStatus = req.getParameter("paymentStatus");

        // ===== DAO =====
        OrderDAO dao = new OrderDAO();
        Map<Order, String> ordersWithName
                = dao.getAllOrdersWithCustomerName(from, to, paymentStatus);

        // ===== SET ATTRIBUTE =====
        req.setAttribute("ordersWithName", ordersWithName);
        req.setAttribute("from", from);
        req.setAttribute("to", to);
        req.setAttribute("paymentStatus", paymentStatus);

        // ===== FORWARD JSP =====
        req.getRequestDispatcher("orderhistory.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        
        HttpSession session = req.getSession();
        User acc = (User) session.getAttribute("acc");

        // ===== CHECK LOGIN + ROLE =====
        if (acc == null
                || (!"EMPLOYEE".equals(acc.getRole()) && !"ADMIN".equals(acc.getRole()))) {
            resp.sendRedirect(req.getContextPath() + "/jsp/auth/login.jsp");
            return;
        }

        String action = req.getParameter("action");

        if ("updateStatus".equals(action)) {
            int orderID = Integer.parseInt(req.getParameter("orderID"));
            String newStatus = req.getParameter("status");

            OrderDAO dao = new OrderDAO();
            boolean success = dao.updateOrderStatus(orderID, newStatus);

            if (success) {
                session.setAttribute("success", "Cập nhật trạng thái đơn hàng thành công!");
            } else {
                session.setAttribute("error", "Cập nhật trạng thái thất bại!");
            }
        } else if ("updatePayment".equals(action)) {
            int orderID = Integer.parseInt(req.getParameter("orderID"));
            String newPaymentStatus = req.getParameter("paymentStatus");

            OrderDAO dao = new OrderDAO();
            boolean success = dao.updatePaymentStatus(orderID, newPaymentStatus);

            if (success) {
                session.setAttribute("success", "Cập nhật trạng thái thanh toán thành công!");
            } else {
                session.setAttribute("error", "Cập nhật trạng thái thanh toán thất bại!");
            }
        }

        resp.sendRedirect("orderhistory");
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
