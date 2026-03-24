<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết sản phẩm | SportStore</title>
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/detail.css">
    </head>

    <body class="bg-light">

        <jsp:include page="common/menu.jsp"></jsp:include>

            <div class="container">
                <div class="product-detail-container">
                    <div class="row">
                        <!-- IMAGE -->
                        <div class="col-md-5">
                            <img src="${product.image}"
                             class="img-fluid w-100"
                             alt="${product.productName}">
                    </div>
                    <div class="col-md-7">
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb bg-transparent p-0">
                                <li class="breadcrumb-item">
                                    <a href="home">Trang chủ</a>
                                </li>
                                <li class="breadcrumb-item active">
                                    ${product.productName}
                                </li>
                            </ol>
                        </nav>

                        <h2 class="font-weight-bold">${product.productName}</h2>

                        <p class="price">
                            Giá: <strong>${product.price} $</strong>
                        </p>

                        <hr>

                        <p class="text-muted">${product.description}</p>

                        <c:set var="hasStock" value="false"/>
                        <c:forEach items="${variants}" var="v">
                            <c:if test="${v.stock > 0}">
                                <c:set var="hasStock" value="true"/>
                            </c:if>
                        </c:forEach>

                        <c:choose>

                            <c:when test="${sessionScope.acc != null}">
                                <c:choose>
                                    <c:when test="${hasStock}">
                                        <form action="cart" method="GET" class="mt-4">
                                            <input type="hidden" name="action" value="add">
                                            <div class="form-group">
                                                <label class="font-weight-bold">
                                                    Chọn Size & Màu:
                                                </label>
                                                <select name="pid"
                                                        class="form-control col-md-6"
                                                        required>
                                                    <c:forEach items="${variants}" var="v">
                                                        <option value="${v.variantID}"
                                                                ${v.stock == 0 ? "disabled" : ""}>
                                                            Size: ${v.size}
                                                            - Màu: ${v.color}
                                                            (Kho: ${v.stock})
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <button type="submit"
                                                    class="btn btn-success btn-lg px-5 mt-3">
                                                <i class="fas fa-cart-plus"></i>
                                                Thêm vào giỏ hàng
                                            </button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-danger btn-lg mt-4" disabled>
                                            Hết hàng
                                        </button>
                                        <p class="text-danger mt-2">
                                            Sản phẩm này hiện đã hết hàng.
                                            Vui lòng quay lại sau hoặc chọn sản phẩm khác.
                                        </p>
                                    </c:otherwise>

                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <a href="login"
                                   class="btn btn-warning btn-lg mt-4">
                                    Đăng nhập để mua hàng
                                </a>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </div>
        <!-- ================= REVIEW SECTION ================= -->
        <div class="container mt-5">
            <div class="card shadow-sm p-4">

                <h4 class="mb-4">Đánh giá sản phẩm</h4>

                <!-- Success/Error Messages -->
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.success}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${sessionScope.error}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                
                <c:if test="${avgRating > 0}">
                    <h5>
                        Đánh giá trung bình:
                        <span class="text-warning">
                            ${avgRating} ⭐
                        </span>
                    </h5>
                </c:if>

                <hr>

                <!-- ================= FORM REVIEW ================= -->
                <c:choose>


                    <c:when test="${sessionScope.acc == null}">
                        <a href="login" class="btn btn-warning">
                            Đăng nhập để đánh giá
                        </a>
                    </c:when>


                    <c:when test="${sessionScope.acc != null && not canReview}">
                        <div class="alert alert-danger">
                            Bạn phải mua sản phẩm này trước khi đánh giá.
                        </div>
                    </c:when>


                    <c:when test="${sessionScope.acc != null && canReview}">
                        <form action="review" method="post" class="mb-4">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productID" value="${product.productID}">

                            <div class="form-group">
                                <label>Chọn số sao:</label>
                                <select name="rating" class="form-control col-md-3" required>
                                    <option value="5">5 ⭐ - Rất tốt</option>
                                    <option value="4">4 ⭐ - Tốt</option>
                                    <option value="3">3 ⭐ - Bình thường</option>
                                    <option value="2">2 ⭐ - Tệ</option>
                                    <option value="1">1 ⭐ - Rất tệ</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Nhận xét:</label>
                                <textarea name="comment"
                                          class="form-control"
                                          rows="3"
                                          required></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary">
                                Gửi đánh giá
                            </button>
                        </form>
                    </c:when>

                </c:choose>

                <hr>


                <c:forEach items="${reviews}" var="r">
                    <div class="border rounded p-3 mb-3">

                        <div class="d-flex justify-content-between">
                            <strong>👤 ${r.username}</strong>
                            <small class="text-muted">${r.createdAt}</small>
                        </div>

                        <div class="text-warning">
                            ${r.rating} ⭐
                        </div>

                        <p class="mt-2">${r.comment}</p>

                        
                        <c:if test="${sessionScope.acc != null 
                                      && sessionScope.acc.userID == r.userID}">

                              <form action="review" method="post" class="d-inline"
                                    onsubmit="return confirm('Bạn có chắc muốn xóa đánh giá này?');">
                                  <input type="hidden" name="action" value="delete">
                                  <input type="hidden" name="reviewID" value="${r.reviewID}">
                                  <input type="hidden" name="productID" value="${product.productID}">
                                  <input type="hidden" name="redirectTo" value="detail">
                                  <button class="btn btn-sm btn-danger">
                                      Xóa đánh giá của tôi
                                  </button>
                              </form>

                        </c:if>

                    </div>
                </c:forEach>

            </div>
        </div>

        <jsp:include page="common/footer.jsp"></jsp:include>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/js/all.min.js"></script>
    </body>
</html>