package controller;

import entity.Category;
import entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.CategoryDAO;
import model.ProductDAO;

@WebServlet(name = "HomeController", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // ===== DAO =====
        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        // ===== PARAM =====
        String cidRaw = request.getParameter("cid");
        String txtSearch = request.getParameter("txtSearch");
        String indexRaw = request.getParameter("index");

        int index = 1;
        if (indexRaw != null) {
            try {
                index = Integer.parseInt(indexRaw);
            } catch (NumberFormatException e) {
                index = 1;
            }
        }

        List<Product> products;
        int endPage = 0;

        // ===== BUSINESS LOGIC =====
        if (txtSearch != null && !txtSearch.trim().isEmpty()) {
            // 🔍 SEARCH
            products = productDAO.search(txtSearch.trim());

        } else if (cidRaw != null && !cidRaw.trim().isEmpty()) {
            // 📂 FILTER BY CATEGORY
            try {
                int cid = Integer.parseInt(cidRaw);
                products = productDAO.getProductsByCategory(cid);
            } catch (NumberFormatException e) {
                products = productDAO.getNext6Product(index);
                endPage = productDAO.getTotalPage();
            }

        } else {
            // 🏠 HOME + PAGINATION
            products = productDAO.getNext6Product(index);
            endPage = productDAO.getTotalPage();
        }

        // ===== CATEGORY MENU =====
        List<Category> categories = categoryDAO.getAll();

        // ===== SET ATTRIBUTE =====
        request.setAttribute("data", products);       // danh sách sản phẩm (STOCK MỚI)
        request.setAttribute("categories", categories);
        request.setAttribute("tag", cidRaw);
        request.setAttribute("txtS", txtSearch);
        request.setAttribute("endP", endPage);
        request.setAttribute("saveIndex", index);

        // ===== FORWARD =====
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Home Controller";
    }
}