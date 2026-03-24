<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Đăng nhập - Sport Store</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>
    <body>

        <div class="glass-container">
            <form action="${pageContext.request.contextPath}/login" method="post">
                <h3>Login</h3>

                <c:if test="${not empty error}">
                    <p class="text-danger">${error}</p>
                </c:if>

                <div class="input-group">
                    <input name="username" type="text" required>
                    <label style="color:greenyellow; font-size:20px;">Tên đăng nhập</label>
                </div>

                <div class="input-group">
                    <input name="password" type="password" required>
                    <label style="color:greenyellow; font-size:20px;">Mật khẩu</label>
                </div>

                <div class="remember-forgot">
                    <label><input type="checkbox" name="remember"> Remember me</label>
                    <a href="${pageContext.request.contextPath}/jsp/auth/ChangePassword.jsp">Change Password?</a>
                </div>

                <button type="submit">Login</button>

                <div class="register-link">
                    <p>Chưa có tài khoản? 
                        <a href="${pageContext.request.contextPath}/jsp/auth/register.jsp">
                            Đăng ký
                        </a>
                    </p>
                </div>

                <div style="text-align:center; margin-top:10px;">
                    <a href="${pageContext.request.contextPath}/resetpassword"
                       style="color:#ff4d4d; font-weight:bold; text-decoration:none;">
                        Quên mật khẩu?
                    </a>
                </div>
            </form>
        </div>

    </body>
</html>