<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Order Detail | Admin</title>
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderdetail.css">
    </head>
    <body>

        <!-- NAVBAR -->
        <nav class="navbar navbar-dark bg-dark mb-4 shadow">
            <div class="container-fluid">
                <a class="navbar-brand" href="${pageContext.request.contextPath}/orderhistory">
                    <strong>SportStore Admin</strong>
                </a>
                <div class="text-white">
                    Xin chào, <strong>${sessionScope.acc.fullName}</strong>
                    |
                    <a href="${pageContext.request.contextPath}/logout"
                       class="text-warning ml-2">
                        <i class="fas fa-sign-out-alt"></i> LOGOUT
                    </a>
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="box mb-4">
                <h3 class="text-primary mb-3">
                    <i class="fas fa-file-invoice"></i>
                    Chi tiết đơn hàng #${orderID}
                </h3>

                <a href="${pageContext.request.contextPath}/orderhistory"
                   class="btn btn-secondary mb-3 shadow-sm">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>

                <c:if test="${not empty customerInfo && not empty orderInfo}">
                <div class="card mb-4 shadow-sm border-0">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-user-circle"></i> Thông tin Người Đặt Hàng & Giao Hàng</h5>
                    </div>
                    <div class="card-body bg-light">
                        <div class="row">
                            <div class="col-md-6 border-right">
                                <p class="mb-2"><i class="fas fa-user text-muted mr-2"></i><strong>Khách hàng:</strong> ${customerInfo.fullName}</p>
                                <p class="mb-2"><i class="fas fa-envelope text-muted mr-2"></i><strong>Email:</strong> <a href="mailto:${customerInfo.email}">${customerInfo.email}</a></p>
                                <p class="mb-0"><i class="fas fa-money-check-alt text-muted mr-2"></i><strong>Thanh toán:</strong> 
                                    <span class="badge badge-${orderInfo.paymentStatus == 'Đã thanh toán' ? 'success' : (orderInfo.paymentStatus == 'Chưa thanh toán' ? 'danger' : 'warning')} px-2 py-1">${orderInfo.paymentStatus}</span>
                                </p>
                            </div>
                            <div class="col-md-6 pl-md-4">
                                <p class="mb-2"><i class="fas fa-phone-alt text-muted mr-2"></i><strong>SĐT Nhận Hàng:</strong> ${orderInfo.phoneNumber != null && orderInfo.phoneNumber.length() > 0 ? orderInfo.phoneNumber : 'Không cung cấp'}</p>
                                <p class="mb-2"><i class="fas fa-map-marker-alt text-muted mr-2"></i><strong>Địa Chỉ Giao:</strong> ${orderInfo.shippingAddress != null && orderInfo.shippingAddress.length() > 0 ? orderInfo.shippingAddress : 'Không cung cấp'}</p>
                                <p class="mb-0"><i class="fas fa-truck text-muted mr-2"></i><strong>Trạng thái đơn:</strong> 
                                    <span class="badge badge-primary px-2 py-1">${orderInfo.status}</span>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
                </c:if>

                <div class="table-responsive">
                    <table class="table table-bordered table-hover shadow-sm">
                        <thead class="thead-light">
                            <tr>
                                <th style="width: 10%">Variant ID</th>
                                <th style="width: 40%">Thông tin sản phẩm</th> <th style="width: 10%">Số lượng</th>
                                <th style="width: 20%">Đơn giá</th>
                                <th style="width: 20%">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="grandTotal" value="0"/>

                            <c:forEach var="d" items="${orderDetails}">
                                <tr>
                                    <td class="text-muted font-italic">#${d.variantID}</td>
                                    <td>
                                        <div class="font-weight-bold text-dark">${d.productName}</div>
                                        <div class="small text-secondary mt-1">
                                            <span class="badge badge-info mr-2">Màu: ${d.color}</span>
                                            <span class="badge badge-secondary">Size: ${d.size}</span>
                                        </div>
                                    </td>
                                    <td class="text-center font-weight-bold">${d.quantity}</td>
                                    <td>
                                        <fmt:formatNumber value="${d.unitPrice}" type="number"/> $
                                    </td>
                                    <td class="text-danger font-weight-bold">
                                        <fmt:formatNumber value="${d.quantity * d.unitPrice}" type="number"/> $
                                    </td>
                                </tr>
                                <c:set var="grandTotal" value="${grandTotal + (d.quantity * d.unitPrice)}"/>
                            </c:forEach>

                            <c:if test="${empty orderDetails}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted py-5">
                                        <i class="fas fa-folder-open fa-3x mb-3 d-block"></i>
                                        Không có chi tiết đơn hàng
                                    </td>
                                </tr>
                            </c:if>
                        </tbody>

                        <tfoot>
                            <tr class="bg-light">
                                <th colspan="4" class="text-right text-uppercase pt-3">Tổng cộng thanh toán:</th>
                                <th class="text-danger font-weight-bold h4 pt-3">
                                    <fmt:formatNumber value="${grandTotal}" type="number"/> $
                                </th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>


        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>