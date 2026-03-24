<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Hệ thống Quản lý Sản phẩm | SportStore</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/productmanager.css">
    </head>
    <body>

        <!-- NAVBAR -->
        <nav class="navbar navbar-dark bg-dark mb-4 shadow">
            <div class="container-fluid">
                <a class="navbar-brand" href="manager"><strong>SportStore Admin</strong></a>
                <div class="text-white">
                    Chào, <strong>${sessionScope.acc.fullName}</strong> |
                    <a href="jsp/auth/login.jsp" class="text-warning ml-2">
                        <i class="fas fa-sign-out-alt"></i> LOGOUT
                    </a>
                </div>
            </div>
        </nav>

        <div class="container-fluid">

            <!-- ALERT -->
            <div class="mt-2">
                <c:if test="${param.msg == 'add_success'}">
                    <div class="alert alert-success alert-dismissible fade show">
                        Thêm sản phẩm thành công!
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>

                <c:if test="${param.msg == 'update_success'}">
                    <div class="alert alert-info alert-dismissible fade show">
                        Cập nhật sản phẩm thành công!
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>

                <c:if test="${param.msg == 'delete_success'}">
                    <div class="alert alert-success alert-dismissible fade show">
                        Xóa sản phẩm thành công!
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>

                <c:if test="${param.msg == 'add_fail' || param.msg == 'update_fail' || param.msg == 'delete_fail'}">
                    <div class="alert alert-danger alert-dismissible fade show">
                        Thao tác thất bại. Vui lòng kiểm tra lại!
                        <button type="button" class="close" data-dismiss="alert">&times;</button>
                    </div>
                </c:if>
            </div>

            <!-- TABLE -->
            <div class="table-container">
                <div class="row mb-4 align-items-center">
                    <div class="col-md-3">
                        <h2 class="text-primary m-0">Kho sản phẩm</h2>
                    </div>

                    <!-- SEARCH -->
                    <div class="col-md-4">
                        <form action="manager" method="get" class="search-box">
                            <div class="input-group shadow-sm">
                                <input name="txtSearch"
                                       type="text"
                                       class="form-control"
                                       placeholder="Tìm theo tên sản phẩm..."
                                       value="${param.txtSearch}">
                                <div class="input-group-append">
                                    <button class="btn btn-primary" type="submit">
                                        <i class="fas fa-search"></i> Tìm
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div class="col-md-5 text-right">

                         <a href="${pageContext.request.contextPath}/view-reviews"
                           class="btn btn-info shadow mr-2">
                            <i class="fas fa-receipt"></i> Review
                        </a>
                           
                        <a href="${pageContext.request.contextPath}/dashboard"
                           class="btn btn-info shadow mr-2">
                            <i class="fas fa-receipt"></i> Overview
                        </a>

                        <!-- ORDER HISTORY -->
                        <a href="${pageContext.request.contextPath}/orderhistory"
                           class="btn btn-info shadow mr-2">
                            <i class="fas fa-receipt"></i> Order History
                        </a>

                        <!-- ADD PRODUCT -->
                        <button class="btn btn-success shadow" data-toggle="modal" data-target="#addModal">
                            <i class="fas fa-plus-circle"></i> Thêm mới
                        </button>

                    </div>
                </div>

                <table class="table table-hover table-striped border">
                    <thead class="bg-light">
                        <tr>
                            <th>ID</th>
                            <th>Ảnh</th>
                            <th>Tên sản phẩm</th>
                            <th>Giá</th>
                            <th>Trạng thái</th>
                            <th class="text-center">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty data}">
                                <c:forEach items="${data}" var="p">
                                    <tr>
                                        <td>${p.productID}</td>
                                        <td>
                                            <img src="${p.image}" class="product-img">
                                        </td>
                                        <td><strong>${p.productName}</strong></td>
                                        <td class="text-danger font-weight-bold">${p.price} $</td>
                                        <td>
                                            <span class="badge ${p.status eq 'ACTIVE' ? 'badge-success' : 'badge-secondary'} p-2">
                                                ${p.status}
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <a href="edit-product?pid=${p.productID}" class="btn btn-warning btn-sm mx-1">
                                                <i class="fas fa-edit"></i> Sửa
                                            </a>
                                            <a href="delete-product?pid=${p.productID}"
                                               class="btn btn-danger btn-sm mx-1"
                                               onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?')">
                                                <i class="fas fa-trash"></i> Xóa
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>

                            <c:otherwise>
                                <tr>
                                    <td colspan="6" class="text-center text-muted py-4">
                                        ❌ Không tìm thấy sản phẩm phù hợp
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- ADD MODAL -->
        <div class="modal fade" id="addModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">

                    <!-- ⚠️ KHÔNG multipart -->
                    <form action="add-product" method="post">

                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">Thêm sản phẩm mới</h5>
                            <button type="button" class="close text-white" data-dismiss="modal">&times;</button>
                        </div>

                        <div class="modal-body">

                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label>Tên sản phẩm</label>
                                    <input name="name" class="form-control" required>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label>Danh mục</label>
                                    <select name="category" class="form-control">
                                        <c:forEach items="${categories}" var="c">
                                            <option value="${c.categoryID}">
                                                ${c.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6 form-group">
                                    <label>Giá</label>
                                    <input name="price" type="number" step="0.01" min="0"
                                           class="form-control" required>
                                </div>
                                <div class="col-md-6 form-group">
                                    <label>Hình ảnh (link)</label>
                                    <input name="image" class="form-control">
                                </div>
                            </div>

                            <div class="row justify-content-center">
                                <div class="col-md-6 form-group">
                                    <label>Trạng thái</label>
                                    <select name="status" class="form-control">
                                        <option value="ACTIVE">Hoạt động</option>
                                        <option value="INACTIVE">Ngừng bán</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label>Mô tả</label>
                                <textarea name="description" rows="4" class="form-control"></textarea>
                            </div>

                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-success">Lưu</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>