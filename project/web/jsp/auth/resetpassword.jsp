<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Reset Password</title>
        <link rel="stylesheet"
              href="${pageContext.request.contextPath}/css/resetpassword.css">
    </head>

    <body>

        <a href="${pageContext.request.contextPath}/login"
           class="back-link">Back</a>

        <div class="container">
            <h2>Reset Password</h2>

            <!-- HIỂN THỊ MESSAGE -->
            <c:if test="${not empty message}">
                <div class="message error">
                    ${message}
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/resetpassword"
                  method="post">

                <!-- USERNAME -->
                <div class="form-group">
                    <label>User Name</label>
                    <input type="text"
                           name="username"
                           value="${param.username}"
                           required
                           placeholder="Enter your username">
                </div>

                <!-- NEW PASSWORD -->
                <div class="form-group">
                    <label>New Password</label>
                    <input type="password"
                           name="newPassword"
                           minlength="6"
                           maxlength="30"
                           required
                           placeholder="••••••••">
                </div>

                <!-- CONFIRM PASSWORD -->
                <div class="form-group">
                    <label>Confirm Password</label>
                    <input type="password"
                           name="confirmPassword"
                           minlength="6"
                           maxlength="30"
                           required
                           placeholder="••••••••">
                </div>

                <button type="submit">Reset</button>
            </form>
        </div>

    </body>
</html>