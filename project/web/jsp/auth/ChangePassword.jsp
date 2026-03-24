<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đổi Mật Khẩu - Premium UI</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/changepassword.css">
    </head>

    <body>

        <a href="${pageContext.request.contextPath}/login" class="back-link">Back</a>

        <div class="password-card">
            <h2>Đổi mật khẩu</h2>

            <c:if test="${not empty error}">
                <div class="alert-error">
                    ⚠️ ${error}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/changepassword" method="post">

                <div class="form-group">
                    <label>Tên đăng nhập</label>
                    <input type="text" name="username" value="${param.username}" placeholder="Nhập username" required>
                </div>

                <div class="form-group">
                    <label>Mật khẩu cũ</label>
                    <input type="password" name="oldPassword" placeholder="••••••" required>
                </div>

                <div class="form-group">
                    <label>Mật khẩu mới</label>
                    <input type="password" name="newPassword"  minlength="6"
                           maxlength="30"placeholder="Mật khẩu mới" required>
                </div>

                <div class="form-group">
                    <label>Xác nhận lại</label>
                    <input type="password" name="confirmPassword"  minlength="6"
                           maxlength="30"placeholder="Nhập lại mật khẩu mới" required>
                </div>

                <button type="submit" class="btn-save">
                    CẬP NHẬT MẬT KHẨU
                </button>
            </form>
        </div>

    </body>
</html>
