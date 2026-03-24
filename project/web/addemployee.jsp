<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hệ thống - Thêm nhân viên</title>

    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/addemployee.css">
</head>

<body class="flex items-center justify-center p-4">

<div class="flex flex-col md:flex-row items-center gap-10 max-w-5xl w-full">

    <!-- LEFT SIDE -->
    <div class="text-white flex flex-col items-center md:items-start gap-6 w-full md:w-1/3">
        <div class="flex items-center gap-3">
            <i class="fas fa-star text-3xl"></i>
            <h2 class="text-4xl font-bold tracking-widest">STARTECH</h2>
        </div>

        <div class="flex flex-col w-full gap-3">
            <div class="glass-effect text-center py-3 rounded-full font-bold text-xs tracking-widest border-2 border-white/50">
                THÊM NHÂN VIÊN
            </div>

            <a href="manager"
               class="text-center py-3 rounded-full font-bold text-xs tracking-widest hover:bg-white/10 transition-all border border-white/20">
                QUẢN LÝ
            </a>
        </div>
    </div>

    <!-- RIGHT SIDE -->
    <div class="bg-white rounded-[2.5rem] shadow-2xl p-10 md:p-12 w-full md:w-2/3 max-w-lg">

        <h3 class="text-3xl font-bold text-[#831843] mb-2">
            Thêm nhân viên
        </h3>

        <p class="text-gray-400 text-sm mb-8">
            Tạo tài khoản nhân viên mới vào cấu trúc hệ thống.
        </p>

        <!-- HIỂN THỊ LỖI -->
        <c:if test="${not empty error}">
            <div class="mb-5 p-3 bg-red-50 border-l-4 border-red-500 text-red-700 text-sm rounded shadow-sm">
                <i class="fas fa-exclamation-triangle mr-2"></i>
                ${error}
            </div>
        </c:if>

        <form action="add-employee" method="post" class="space-y-4">

            <!-- USERNAME -->
            <div class="relative">
                <span class="absolute left-5 top-1/2 -translate-y-1/2 text-gray-400">
                    <i class="far fa-user"></i>
                </span>
                <input type="text"
                       name="username"
                       value="${param.username}"
                       placeholder="Username"
                       required
                       class="w-full pl-12 pr-5 py-3.5 border border-gray-100 bg-gray-50/50 rounded-full focus:ring-2 focus:ring-purple-200 focus:outline-none transition-all">
            </div>

            <!-- PASSWORD -->
            <div class="relative">
                <span class="absolute left-5 top-1/2 -translate-y-1/2 text-gray-400">
                    <i class="fas fa-lock text-sm"></i>
                </span>
                <input type="password"
                       name="password"
                       placeholder="Mật khẩu"
                       minlength="6"
                       required
                       class="w-full pl-12 pr-5 py-3.5 border border-gray-100 bg-gray-50/50 rounded-full focus:ring-2 focus:ring-purple-200 focus:outline-none transition-all">
            </div>

            <!-- FULL NAME -->
            <div class="relative">
                <span class="absolute left-5 top-1/2 -translate-y-1/2 text-gray-400">
                    <i class="far fa-address-card"></i>
                </span>
                <input type="text"
                       name="fullName"
                       value="${param.fullName}"
                       placeholder="Họ và tên"
                       required
                       class="w-full pl-12 pr-5 py-3.5 border border-gray-100 bg-gray-50/50 rounded-full focus:ring-2 focus:ring-purple-200 focus:outline-none transition-all">
            </div>

            <!-- EMAIL -->
            <div class="relative">
                <span class="absolute left-5 top-1/2 -translate-y-1/2 text-gray-400">
                    <i class="far fa-envelope"></i>
                </span>
                <input type="email"
                       name="email"
                       value="${param.email}"
                       placeholder="Địa chỉ Email"
                       required
                       class="w-full pl-12 pr-5 py-3.5 border border-gray-100 bg-gray-50/50 rounded-full focus:ring-2 focus:ring-purple-200 focus:outline-none transition-all">
            </div>

            <!-- CONFIRM CHECKBOX -->
            <div class="flex items-center gap-2 px-2 py-2 text-xs text-gray-500">
                <input type="checkbox"
                       id="confirm"
                       required
                       class="w-4 h-4 rounded border-gray-300 text-purple-600 focus:ring-purple-500">
                <label for="confirm"
                       class="cursor-pointer select-none">
                    Tôi xác nhận các thông tin trên là chính xác
                </label>
            </div>

            <!-- BUTTON -->
            <div class="pt-4 flex flex-col gap-4">
                <button type="submit"
                        class="btn-startech w-full text-white font-bold py-4 rounded-full shadow-lg uppercase tracking-widest text-sm">
                    XÁC NHẬN THÊM
                </button>

                <a href="manager"
                   class="text-center text-gray-400 hover:text-[#831843] font-bold text-xs uppercase transition-colors">
                    <i class="fas fa-arrow-left mr-1"></i>
                    Quay lại trang quản lý
                </a>
            </div>

        </form>
    </div>
</div>

</body>
</html>