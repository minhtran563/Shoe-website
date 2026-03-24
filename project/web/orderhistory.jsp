<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Lịch sử đơn hàng | SportStore Management</title>

        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/orderhistory.css">
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

        <div class="container-fluid px-4 main-content">

            <a href="${pageContext.request.contextPath}/manager" class="back-link">
                <i class="fas fa-arrow-left me-2"></i> Back to manager
            </a>

            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <div class="row">
                <div class="col-12">
                    <h3 class="fw-bold text-dark mb-4">Lịch sử đơn hàng</h3>

                    <div class="custom-card p-4">
                        <form method="get" class="row g-3 align-items-end">
                            <div class="col-md-3">
                                <label class="form-label small fw-bold text-muted">TỪ NGÀY</label>
                                <input type="date" name="from" class="form-control shadow-sm" value="${param.from}">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold text-muted">ĐẾN NGÀY</label>
                                <input type="date" name="to" class="form-control shadow-sm" value="${param.to}">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold text-muted">THANH TOÁN</label>
                                <select name="paymentStatus" class="form-select shadow-sm">
                                    <option value="">Tất cả</option>
                                    <option value="PAID" ${param.paymentStatus=='PAID'?'selected':''}>Đã thanh toán</option>
                                    <option value="UNPAID" ${param.paymentStatus=='UNPAID'?'selected':''}>Chưa thanh toán</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <button type="submit" class="btn btn-primary w-100 fw-bold shadow">
                                    <i class="fas fa-filter me-2"></i> LỌC DỮ LIỆU
                                </button>
                            </div>
                        </form>
                    </div>

                    <div class="custom-card">
                        <div class="card-header-main d-flex justify-content-between align-items-center">
                            <h5 class="m-0 fw-bold text-primary">Danh sách đơn hàng</h5>
                            <span class="badge bg-light text-dark border px-3 py-2">${ordersWithName.size()} đơn hàng</span>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Khách hàng & Liên hệ</th>
                                        <th>Ngày đặt</th>
                                        <th>Tổng tiền</th>
                                        <th>Thanh toán</th>
                                        <th>Trạng thái</th>
                                        <th class="text-center">Hành động</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="entry" items="${ordersWithName}">
                                        <tr>
                                            <td class="fw-bold">#${entry.key.orderID}</td>
                                            <td>
                                                <div class="fw-bold text-dark mb-1">${entry.value}</div>
                                                <div class="phone-highlight">
                                                    <i class="fas fa-phone-alt"></i>
                                                    ${entry.key.phoneNumber}
                                                </div>
                                            </td>
                                            <td class="small text-muted">
                                                <fmt:formatDate value="${entry.key.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </td>
                                            <td class="fw-bold text-danger">
                                                <fmt:formatNumber value="${entry.key.totalPrice}" type="number" groupingUsed="true"/> $
                                            </td>
                                            <td>
                                                <form action="orderhistory" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="updatePayment">
                                                    <input type="hidden" name="orderID" value="${entry.key.orderID}">
                                                    <select name="paymentStatus" class="form-select form-select-sm d-inline-block w-auto payment-select" 
                                                            onchange="this.form.submit()"
                                                            style="min-width: 100px;">
                                                        <option value="UNPAID" ${entry.key.paymentStatus=='UNPAID' ? 'selected' : ''}>UNPAID</option>
                                                        <option value="PAID" ${entry.key.paymentStatus=='PAID' ? 'selected' : ''}>PAID</option>
                                                    </select>
                                                </form>
                                            </td>
                                            <td>
                                                <form action="orderhistory" method="post" class="d-inline">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="orderID" value="${entry.key.orderID}">
                                                    <select name="status" class="form-select form-select-sm d-inline-block w-auto" 
                                                            onchange="this.form.submit()"
                                                            style="min-width: 120px;">
                                                        <option value="Pending" ${entry.key.status=='Pending' ? 'selected' : ''}>Pending</option>
                                                        <option value="Processing" ${entry.key.status=='Processing' ? 'selected' : ''}>Processing</option>
                                                        <option value="Shipping" ${entry.key.status=='Shipping' ? 'selected' : ''}>Shipping</option>
                                                        <option value="Completed" ${entry.key.status=='Completed' ? 'selected' : ''}>Completed</option>
                                                        <option value="Cancelled" ${entry.key.status=='Cancelled' ? 'selected' : ''}>Cancelled</option>
                                                    </select>
                                                </form>
                                            </td>
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/orderdetail?orderID=${entry.key.orderID}" 
                                                   class="btn btn-info btn-sm text-white rounded-pill px-3 shadow-sm">
                                                    <i class="fas fa-eye"></i> Chi tiết
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${empty ordersWithName}">
                                        <tr>
                                            <td colspan="7" class="text-center py-5 text-muted small italic">Không tìm thấy đơn hàng nào khớp với bộ lọc.</td>
                                        </tr>
                                    </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <c:if test="${not empty orderDetails}">
                        <div class="custom-card border-start border-primary border-4">
                            <div class="card-header-main bg-light">
                                <h5 class="m-0 fw-bold text-primary"><i class="fas fa-shopping-cart me-2"></i>Chi tiết đơn hàng #${param.orderID}</h5>
                            </div>
                            <div class="p-0">
                                <table class="table align-middle mb-0">
                                    <thead class="table-light">
                                        <tr>
                                            <th class="ps-4">Variant ID</th>
                                            <th>Số lượng</th>
                                            <th>Đơn giá</th>
                                            <th class="text-end pe-4">Thành tiền</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="d" items="${orderDetails}">
                                            <tr>
                                                <td class="ps-4 fw-bold text-primary">#${d.variantID}</td>
                                                <td><span class="badge bg-secondary rounded-pill">${d.quantity}</span></td>
                                                <td><fmt:formatNumber value="${d.unitPrice}" groupingUsed="true"/> $</td>
                                                <td class="text-end pe-4 fw-bold text-danger">
                                                    <fmt:formatNumber value="${d.quantity * d.unitPrice}" groupingUsed="true"/> $
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>