<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Giỏ hàng của bạn</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    </head>
    <body>

        <jsp:include page="common/menu.jsp"></jsp:include>

            <div class="container mt-5">
                <h2 class="mb-4"><i class="fa fa-shopping-cart"></i> Giỏ hàng của bạn</h2>

                <!-- ===== THÔNG BÁO LỖI (MUA QUÁ SỐ LƯỢNG) ===== -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger">
                    ${error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty cartItems}">
                    <table class="table table-hover table-bordered">
                        <thead class="thead-dark text-center">
                            <tr>
                                <th>Sản phẩm</th>
                                <th>Biến thể (Size/Màu)</th>
                                <th>Hình ảnh</th>
                                <th>Số lượng</th>
                                <th>Thành tiền</th>
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="total" value="0" />

                            <c:forEach items="${cartItems}" var="item">

                                <tr class="text-center">
                                    <td class="text-left font-weight-bold">
                                        ${item.product.productName}
                                    </td>

                                    <td>
                                        <span class="badge badge-info">
                                            Size: ${item.variant.size} - Màu: ${item.variant.color}
                                        </span>
                                    </td>

                                    <td>
                                        <img src="${item.product.image}" width="60" class="img-thumbnail">
                                    </td>

                                    <td>

                                        <a href="cart?action=minus&pid=${item.variant.variantID}"
                                           class="btn btn-sm btn-outline-secondary">-</a>

                                        <span class="mx-2 font-weight-bold">
                                            ${item.quantity}
                                        </span>

                                        <!-- NÚT TĂNG (DISABLE NẾU ĐẠT STOCK) -->
                                        <c:choose>
                                            <c:when test="${item.quantity >= item.variant.stock}">
                                                <button class="btn btn-sm btn-outline-secondary" disabled>+</button>
                                                <div>
                                                    <small class="text-danger">Đã đạt tối đa</small>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="cart?action=add&pid=${item.variant.variantID}"
                                                   class="btn btn-sm btn-outline-secondary">+</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>

                                    <td class="text-danger">
                                        ${item.price * item.quantity} $
                                    </td>

                                    <td>
                                        <a href="cart?action=remove&pid=${item.variant.variantID}"
                                           class="btn btn-danger btn-sm"
                                           onclick="return confirm('Xóa sản phẩm này?')">
                                            <i class="fa fa-trash"></i> Xóa
                                        </a>
                                    </td>
                                </tr>

                                <c:set var="total"
                                       value="${total + (item.price * item.quantity)}" />
                            </c:forEach>
                        </tbody>
                    </table>

                    <div class="text-right mt-3">
                        <h4>
                            Tổng cộng:
                            <span class="text-danger">${total} $</span>
                        </h4>

                        <a href="home" class="btn btn-secondary">
                            Tiếp tục mua hàng
                        </a>

                        <a href="${pageContext.request.contextPath}/checkout"
                           class="btn btn-success">
                            Thanh toán ngay
                        </a>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="alert alert-warning text-center">
                        Giỏ hàng đang trống!
                        <a href="home">Mua ngay</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <jsp:include page="common/footer.jsp"></jsp:include>

    </body>
</html>