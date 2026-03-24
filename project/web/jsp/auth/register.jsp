<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Startech - Đăng ký</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>

<body>

<div class="main-card">

    <!-- LEFT -->
    <div class="left-side d-none d-md-flex">
        <div class="brand-name">
            <i class="fas fa-star"></i> STARTECH
        </div>
        <a class="btn-outline-custom">REGISTER</a>
        <a href="${pageContext.request.contextPath}/login" class="btn-outline-custom">
            LOGIN
        </a>
    </div>

    <!-- RIGHT -->
    <div class="right-side">
        <div class="form-header">
            <h2>Register</h2>
            <p>Tạo tài khoản mới dựa trên cấu trúc hệ thống.</p>
        </div>

        <!-- Hiển thị lỗi tổng -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger text-center">
                ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">

            <!-- Full Name -->
            <div class="mb-3">
                <div class="input-group-custom">
                    <i class="far fa-id-card"></i>
                    <input name="fullName"
                           type="text"
                           class="form-control"
                           placeholder="Full Name"
                           value="${param.fullName}"
                           required>
                </div>
            </div>

            <!-- Username -->
            <div class="mb-3">
                <div class="input-group-custom">
                    <i class="far fa-user"></i>
                    <input name="username"
                           type="text"
                           class="form-control"
                           placeholder="Username"
                           value="${param.username}"
                           required>
                </div>
            </div>

            <!-- Email -->
            <div class="mb-3">
                <div class="input-group-custom">
                    <i class="far fa-envelope"></i>
                    <input name="email"
                           type="email"
                           class="form-control"
                           placeholder="name@gmail.com"
                           value="${param.email}"
                           required>
                </div>
            </div>

            <!-- Password -->
            <div class="mb-3">
                <div class="input-group-custom">
                    <i class="fa fa-lock"></i>
                    <input name="password"
                           type="password"
                           class="form-control"
                           placeholder="Password"
                           required>
                </div>
            </div>

            <!-- Confirm Password -->
            <div class="mb-3">
                <div class="input-group-custom">
                    <i class="fa fa-shield-alt"></i>
                    <input name="rePassword"
                           type="password"
                           class="form-control"
                           placeholder="Confirm Password"
                           required>
                </div>
            </div>

            <!-- Terms -->
            <div class="form-check mb-3 small">
                <input class="form-check-input" type="checkbox" id="terms" required>
                <label class="form-check-label" for="terms">
                    Tôi đồng ý với các điều khoản sử dụng
                </label>
            </div>

            <button type="submit" class="btn btn-primary w-100">
                SIGN UP
            </button>
        </form>
    </div>
</div>

</body>
</html>