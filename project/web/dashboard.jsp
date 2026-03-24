<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Admin Dashboard | SportStore</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>

        <nav class="navbar navbar-expand-lg navbar-admin">
            <div class="container-fluid d-flex justify-content-between align-items-center p-0">
                <a class="navbar-brand" href="#">SportStore Admin</a>

                <div class="nav-user-info d-flex align-items-center">
                    <span>Chào, <strong>${sessionScope.acc.fullName}</strong> | </span>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-link">
                        <i class="fas fa-sign-out-alt"></i> LOGOUT
                    </a>
                </div>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="manager" class="text-decoration-none">Manager</a></li>
                            <li class="breadcrumb-item active">Dashboard</li>
                        </ol>
                    </nav>
                    <h2 class="fw-bold m-0 text-dark">📊 Dashboard Overview</h2>
                </div>
<!--                <button class="btn btn-primary shadow-sm" onclick="window.print()">
                    <i class="fas fa-download me-2"></i> Xuất báo cáo
                </button>-->
            </div>

            <div class="row g-4 mb-5">
                <div class="col-md-4">
                    <div class="card stat-card bg-primary text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-white-50 small mb-1 fw-bold">TỔNG SẢN PHẨM</p>
                                <h3 class="fw-bold mb-0">${totalProducts}</h3>
                            </div>
                            <div class="icon-shape"><i class="fas fa-boxes"></i></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card bg-success text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-white-50 small mb-1 fw-bold">SẢN PHẨM ĐÃ BÁN</p>
                                <h3 class="fw-bold mb-0">${soldProducts}</h3>
                            </div>
                            <div class="icon-shape"><i class="fas fa-shopping-cart"></i></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card bg-warning text-white p-3">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="text-white-50 small mb-1 fw-bold">TỔNG DOANH THU</p>
                                <h3 class="fw-bold mb-0">
                                    <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/>
                                </h3>
                            </div>
                            <div class="icon-shape"><i class="fas fa-wallet"></i></div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-lg-6">
                    <div class="table-container">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold m-0"><i class="fas fa-users text-primary me-2"></i>Khách hàng mới</h5>
                            <span class="badge bg-light text-primary">Member</span>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Khách hàng</th>
                                        <th>Email</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${customerList}" var="c">
                                        <tr>
                                            <td><span class="text-muted small">#${c.userID}</span></td>
                                            <td>
                                                <div class="d-flex align-items-center">
                                                    <div class="user-avatar fw-bold">${c.username.substring(0,1).toUpperCase()}</div>
                                                    <div>
                                                        <div class="fw-bold text-dark">${c.fullName}</div>
                                                        <div class="small text-muted">@${c.username}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td class="small">${c.email}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="col-lg-6">
                    <div class="table-container">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="fw-bold m-0"><i class="fas fa-user-tie text-info me-2"></i>Danh sách nhân viên</h5>
                            <span class="badge bg-info-subtle text-info">Staff</span>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Họ tên</th>
                                        <th>Email</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${employeeList}" var="e">
                                        <tr>
                                            <td><span class="text-muted small">#${e.userID}</span></td>
                                            <td class="fw-bold text-dark">${e.fullName}</td>
                                            <td class="small text-muted">${e.email}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>