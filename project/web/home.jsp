<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sport Store - Home</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">

    </head>

    <body>

        <jsp:include page="common/menu.jsp"></jsp:include>
            <div class="container-fluid p-0">
                <div class="hero-banner">
                    <div class="hero-item">
                        <img src="https://xamsneaker.com/wp-content/uploads/nam-nay-dan-tinh-lung-suc-nike-air-jordan-qua-ban.jpeg">
                        <div class="hero-content">
                            <h2>CHÚC MỪNG NĂM MỚI</h2>
                            <p>Welcome everyone to our store!</p>
                            <a href="home" class="btn-hero">Quần áo siêu đẹp giá Tốt!!</a>
                        </div>
                    </div>
                    <div class="hero-item">
                        <img src="https://cdn.kosshop.vn/upload/images/2023/imagespost/images/balo-dung-macbook-air%20%282%29.jpg">
                    </div>
                    <div class="hero-item">
                        <img src="https://product.hstatic.net/200000365171/product/thi_dau_do__3__c4cd6ca7938548f6b91a9593fb32623a.png">
                    </div>
                </div>
            </div>
            <div class="container mt-4">
                <div class="row">
                <jsp:include page="common/left.jsp"></jsp:include>

                    <div class="col-sm-9">
                        <div id="content" class="row">
                        <%-- Vòng lặp hiển thị danh sách sản phẩm --%>
                        <c:forEach items="${data}" var="o">
                            <div class="product col-12 col-md-6 col-lg-4 mb-4">
                                <div class="card h-100">
                                    <%-- Logic hiển thị ảnh: Tự thêm 'images/' nếu không phải link http --%>
                                    <c:set var="imagePath" value="${o.image.startsWith('http') ? o.image : 'images/'.concat(o.image)}" />

                                    <img class="card-img-top" src="${imagePath}" 
                                         onerror="this.src='https://placehold.co/600x400?text=No+Image'" 
                                         alt="${o.productName}">

                                    <div class="card-body d-flex flex-column">
                                        <h5 class="card-title">
                                            <a href="detail?pid=${o.productID}" title="View Product" class="text-dark font-weight-bold">
                                                ${o.productName}
                                            </a>
                                        </h5>
                                        <p class="card-text show_txt text-muted small">${o.description}</p>

                                        <div class="mt-auto">
                                            <div class="row align-items-center">
                                                <div class="col-6 text-center">
                                                    <p class="price-bold-clean mb-0" 
                                                       onclick="animatePrice(this)">
                                                        ${o.price} ₫
                                                    </p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>


                        <%-- Hiển thị thông báo nếu không có sản phẩm --%>
                        <c:if test="${empty data}">
                            <div class="col-12 text-center mt-5">
                                <img src="https://cdn-icons-png.flaticon.com/512/7486/7486744.png" width="100" class="mb-3 opacity-50">
                                <h3 class="text-muted">Không tìm thấy sản phẩm nào!</h3>
                            </div>
                        </c:if>

                        <%-- PHẦN PHÂN TRANG (PAGINATION) --%>
                        <c:if test="${endP > 1}">
                            <div class="col-12 mt-4">
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <%-- Nút trang trước --%>
                                        <li class="page-item ${saveIndex == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="home?index=${saveIndex - 1}">Previous</a>
                                        </li>

                                        <%-- Các con số trang --%>
                                        <c:forEach begin="1" end="${endP}" var="i">
                                            <li class="page-item ${saveIndex == i ? 'active' : ''}">
                                                <a class="page-link" href="home?index=${i}">${i}</a>
                                            </li>
                                        </c:forEach>

                                        <%-- Nút trang sau --%>
                                        <li class="page-item ${saveIndex == endP ? 'disabled' : ''}">
                                            <a class="page-link" href="home?index=${saveIndex + 1}">Next</a>
                                        </li>
                                    </ul>
                                </nav>
                            </div>
                        </c:if>

                    </div> </div> </div> </div> <jsp:include page="common/footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>