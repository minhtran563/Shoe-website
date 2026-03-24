<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Shoes Shop</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

        <!-- Font Awesome -->
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
    </head>

    <body>

        <!-- ===== MENU ===== -->
        <nav class="navbar navbar-expand-md navbar-dark bg-dark">
            <div class="container">

                <!-- LOGO -->
                <a class="navbar-brand" href="home">Shoes</a>

                <!-- SEARCH -->
                <form class="form-inline ml-auto mr-3" action="home" method="get">
                    <input oninput="search(this)"
                           name="txtSearch"
                           type="text"
                           class="form-control mr-2"
                           placeholder="Tìm tên hoặc ID sản phẩm..."
                           value="${param.txtSearch}">
                    <button class="btn btn-primary" type="submit">
                        <i class="fas fa-search"></i> Tìm
                    </button>
                </form>

                <!-- CART -->
                <a class="btn btn-success btn-sm mr-3 cart-btn"
                   href="${pageContext.request.contextPath}/cart">
                    <i class="fa fa-shopping-cart"></i> Cart
                    <span class="badge badge-light">
                        ${sessionScope.cartSize != null ? sessionScope.cartSize : 0}
                    </span>
                </a>

                <!-- LOGIN / REGISTER -->
                <c:if test="${sessionScope.acc == null}">
                    <a class="nav-link text-light mr-2" href="jsp/auth/login.jsp">
                        Login
                    </a>
                    <a class="nav-link text-warning" href="jsp/auth/register.jsp">
                        Register
                    </a>
                </c:if>

                <!-- USER INFO -->
                <c:if test="${sessionScope.acc != null}">
                    <span class="text-light mr-2">
                        ❤️ ${sessionScope.acc.fullName}
                    </span>
                    <a class="nav-link text-danger" href="logout">
                        Logout
                    </a>
                </c:if>

            </div>
        </nav>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>