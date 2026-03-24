<!--Nó tạo đường viền bao quanh bảng và từng ô. sử dụng cả <th> và <td>-->
<!--<table border="1" width="100%">--> 

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý Đánh giá | SportStore Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/view-reviews.css">

</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container-fluid px-4">
            <a class="navbar-brand fw-bold d-flex align-items-center" href="manager">
                <i class="fas fa-running text-warning me-2" style="font-size: 24px;"></i>
                SportStore Admin
            </a>
            <div class="ms-auto d-flex align-items-center">
                <div class="text-white me-3 d-none d-md-block">
                    <span class="opacity-75">Chào, </span> <strong>${sessionScope.acc.fullName}</strong>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-warning btn-sm px-3 rounded-pill">
                    <i class="fas fa-sign-out-alt me-1"></i> LOGOUT
                </a>
            </div>
        </div>
    </nav>

    <div class="main-container">
        <a href="manager" class="back-link">
            <i class="fas fa-arrow-left me-2"></i> Back to Dashboard
        </a>

        <h2 class="page-title">Danh sách đánh giá sản phẩm</h2>

        <div class="table-card">
            <div class="table-responsive">
                <table class="table mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Khách hàng</th>
                            <th>Xếp hạng</th>
                            <th>Nội dung bình luận</th>
                            <th>Ngày đánh giá</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${reviews}">
                            <tr>
                                <td class="review-id">#${r.reviewID}</td>
                                <td>
                                    <div class="user-name"> ${r.fullname}</div>
                                </td>
                                <td>
                                    <span class="rating-badge">
                                        ${r.rating} <i class="fas fa-star ms-1"></i>
                                    </span>
                                </td>
                                <td>
                                    <div class="comment-text">${r.comment}</div>
                                </td>
                                <td>
                                    <div class="text-muted small">${r.createdAt}</div>
                                </td>
                                <td>
                                    <form action="review" method="post" class="d-inline"
                                          onsubmit="return confirm('Bạn có chắc muốn xóa đánh giá này?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="reviewID" value="${r.reviewID}">
                                        <button type="submit" class="btn btn-sm btn-danger">
                                            <i class="fas fa-trash"></i> Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        
                        <c:if test="${empty reviews}">
                            <tr>
                                <td colspan="6" class="text-center py-5 text-muted">
                                    <i class="fas fa-comment-slash fa-2x mb-3 d-block"></i>
                                    Chưa có đánh giá nào được tìm thấy.
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>