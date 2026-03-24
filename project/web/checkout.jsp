<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Thanh toán đơn hàng</title>
        <meta charset="UTF-8">
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
    </head>

    <body style="background:#f8f9fa">

        <jsp:include page="common/menu.jsp"/>

        <div class="container mt-5 mb-5">
            <div class="row">

                <!-- ================= LEFT: ORDER SUMMARY ================= -->
                <div class="col-md-7">
                    <div class="card shadow-sm mb-4">
                        <div class="card-header bg-dark text-white">
                            <h5 class="mb-0">🛒 Đơn hàng của bạn</h5>
                        </div>
                        <div class="card-body p-0">
                            <table class="table mb-0">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Sản phẩm</th>
                                        <th class="text-center">SL</th>
                                        <th class="text-right">Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>

                                    <c:set var="total" value="0"/>

                                    <c:forEach items="${cartItems}" var="item">

                                        <tr>
                                            <td>
                                                <strong>${item.product.productName}</strong><br>
                                                <small class="text-muted">
                                                    Size: ${item.variant.size} |
                                                    Màu: ${item.variant.color}
                                                </small>
                                            </td>

                                            <td class="text-center">
                                                ${item.quantity}
                                            </td>

                                            <td class="text-right">
                                                ${item.price * item.quantity} $
                                            </td>
                                        </tr>

                                        <c:set var="total"
                                               value="${total + (item.price * item.quantity)}"/>

                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>

                        <div class="card-footer text-right">
                            <h5>
                                Tổng cộng:
                                <span class="text-danger font-weight-bold">
                                    ${total} $
                                </span>
                            </h5>
                        </div>
                    </div>
                </div>

                <!-- ================= RIGHT: PAYMENT FORM ================= -->
                <div class="col-md-5">
                    <div class="card shadow-sm">
                        <div class="card-header bg-success text-white">
                            <h5 class="mb-0">📦 Thông tin giao hàng</h5>
                        </div>

                        <div class="card-body">

                            <!-- Hiển thị lỗi nếu có -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger">
                                    ${error}
                                </div>
                            </c:if>

                            <form action="${pageContext.request.contextPath}/checkout"
                                  method="POST">

                                <!-- Customer -->
                                <div class="form-group">
                                    <label>Khách hàng</label>
                                    <input type="text"
                                           class="form-control"
                                           value="${sessionScope.acc.fullName}"
                                           readonly>
                                </div>

                                <!-- Shipping Address -->
                                <div class="form-group">
                                    <label>Địa chỉ giao hàng</label>
                                    <input type="text"
                                           name="shippingAddress"
                                           class="form-control"
                                           placeholder="Nhập địa chỉ giao hàng"
                                           required>
                                </div>

                                <!-- Phone -->
                                <div class="form-group">
                                    <label>Số điện thoại</label>
                                    <input type="text"
                                           name="phoneNumber"
                                           class="form-control"
                                           placeholder="Nhập số điện thoại"
                                           pattern="[0-9]{9,11}"
                                           required>
                                </div>

                                <!-- Payment Method -->
                                <div class="form-group">
                                    <label>Phương thức thanh toán</label>
                                    <select name="paymentMethod"
                                            id="paymentMethod"
                                            class="form-control"
                                            onchange="toggleBankQR()">

                                        <option value="COD">
                                            Thanh toán khi nhận hàng (COD)
                                        </option>

                                        <option value="BANK">
                                            Chuyển khoản ngân hàng
                                        </option>
                                    </select>
                                </div>

                                <!-- BANK QR -->
                                <div id="bankQR"
                                     class="border rounded p-3 mb-3 text-center"
                                     style="display:none; background:#f8f9fa">

                                    <h6 class="text-success mb-3">
                                        🏦 Quét mã QR để chuyển khoản
                                    </h6>

                                    <img src="//img.vietqr.io/image/MB-76621082005-compact.png?accountName=NGUYEN%20HOANG%20LONG"
                                         alt="QR chuyển khoản"
                                         style="max-width:220px">

                                    <p class="mt-3 mb-1">
                                        <strong>Ngân hàng:</strong> MB bank
                                    </p>
                                    <p class="mb-1">
                                        <strong>Chủ TK:</strong> Nguyễn Hoàng Long
                                    </p>
                                    <p class="mb-1">
                                        <strong>Số TK:</strong> 76621082005
                                    </p>
                                </div>

                                <button type="submit"
                                        class="btn btn-success btn-block btn-lg">
                                    ✅ XÁC NHẬN ĐẶT HÀNG
                                </button>

                            </form>

                        </div>
                    </div>
                </div>

            </div>
        </div>

        <!-- JS TOGGLE QR -->
        <script>
            function toggleBankQR() {
                var method = document.getElementById("paymentMethod").value;
                var qr = document.getElementById("bankQR");

                if (method === "BANK") {
                    qr.style.display = "block";
                } else {
                    qr.style.display = "none";
                }
            }
        </script>

        <jsp:include page="common/footer.jsp"/>

    </body>
</html>