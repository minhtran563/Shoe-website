<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Edit Product | SportStore</title>

        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
        <link rel="stylesheet"
              href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>

        <div class="container mt-4">

            <div class="card shadow">
                <div class="card-header bg-warning text-white">
                    <h5 class="mb-0">Chỉnh sửa sản phẩm</h5>
                </div>

                <div class="card-body">
                    <form action="edit-product" method="post">

                        <!-- 🔑 PRODUCT ID -->
                        <input type="hidden" name="productID" value="${st.productID}">
                        <!-- 🔑 CATEGORY (select disabled nên cần hidden) -->
                        <input type="hidden" name="category" value="${st.categoryID}">

                        <!-- CATEGORY NAME FOR JS -->
                        <c:set var="catName" value="" />
                        <c:forEach items="${categories}" var="c">
                            <c:if test="${c.categoryID == st.categoryID}">
                                <c:set var="catName" value="${c.categoryName}" />
                            </c:if>
                        </c:forEach>
                        <input type="hidden" id="productCategoryName" value="${catName}">

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Tên sản phẩm</label>
                                <input name="name" class="form-control"
                                       value="${st.productName}" required>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Danh mục</label>
                                <select class="form-control" disabled>
                                    <c:forEach items="${categories}" var="c">
                                        <option ${c.categoryID == st.categoryID ? 'selected' : ''}>
                                            ${c.categoryName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 form-group">
                                <label>Giá</label>
                                <input name="price" type="number" step="0.01"
                                       class="form-control" value="${st.price}" required>
                            </div>

                            <div class="col-md-6 form-group">
                                <label>Trạng thái</label>
                                <select name="status" class="form-control">
                                    <option value="ACTIVE" ${st.status=='ACTIVE'?'selected':''}>ACTIVE</option>
                                    <option value="INACTIVE" ${st.status=='INACTIVE'?'selected':''}>INACTIVE</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Hình ảnh</label>
                            <input name="image" class="form-control" value="${st.image}">
                        </div>

                        <div class="form-group">
                            <label>Mô tả</label>
                            <textarea name="description" rows="3"
                                      class="form-control">${st.description}</textarea>
                        </div>

                        <button class="btn btn-warning">
                            <i class="fas fa-save"></i> Cập nhật
                        </button>
                        <a href="manager" class="btn btn-secondary ml-2">Quay lại</a>
                    </form>
                </div>
            </div>

            <hr class="my-4">

            <!-- VARIANTS -->
            <div class="d-flex justify-content-between align-items-center">
                <h5 class="text-primary">Biến thể</h5>
                <button class="btn btn-success" data-toggle="modal" data-target="#addVariantModal">
                    <i class="fas fa-plus-circle"></i> Thêm biến thể
                </button>
            </div>

            <table class="table table-bordered mt-3">
                <thead class="thead-light">
                    <tr>
                        <th>Size</th>
                        <th>Màu</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>

                    <c:forEach items="${variants}" var="v">

                        <tr>

                            <td>
                                <input type="text"
                                       name="size"
                                       class="form-control form-control-sm"
                                       value="${v.size}"
                                       form="form${v.variantID}">
                            </td>

                            <td>
                                <input type="text"
                                       name="color"
                                       class="form-control form-control-sm"
                                       value="${v.color}"
                                       form="form${v.variantID}">
                            </td>

                            <td>
                                <input type="number"
                                       step="0.01"
                                       name="price"
                                       class="form-control form-control-sm"
                                       value="${v.price}"
                                       form="form${v.variantID}">
                            </td>

                            <td>
                                <input type="number"
                                       name="stock"
                                       class="form-control form-control-sm"
                                       value="${v.stock}"
                                       form="form${v.variantID}">
                            </td>

                            <td class="text-center">

                                <form id="form${v.variantID}" action="edit-variant" method="post" style="display:inline">

                                    <input type="hidden" name="variantID" value="${v.variantID}">
                                    <input type="hidden" name="productID" value="${st.productID}">

                                    <button class="btn btn-warning btn-sm mr-1">
                                        Save
                                    </button>

                                </form>

                                <a href="delete-variant?variantID=${v.variantID}&productID=${st.productID}"
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Xóa biến thể này?')">
                                    Delete
                                </a>

                            </td>

                        </tr>

                    </c:forEach>

                </tbody>
            </table>
        </div>

        <!-- ADD VARIANT MODAL -->
        <div class="modal fade" id="addVariantModal">
            <div class="modal-dialog">
                <div class="modal-content">
                    <form action="add-variant" method="post">

                        <div class="modal-header bg-success text-white">
                            <h5 class="modal-title">Thêm biến thể</h5>
                            <button class="close text-white" data-dismiss="modal">&times;</button>
                        </div>

                        <div class="modal-body">
                            <input type="hidden" name="productID" value="${st.productID}">

                            <div class="form-group">
                                <label>Size</label>
                                <select name="size" id="sizeSelect" class="form-control" required>
                                    <option value="S" data-type="clothing">S</option>
                                    <option value="M" data-type="clothing">M</option>
                                    <option value="L" data-type="clothing">L</option>
                                    <option value="XL" data-type="clothing">XL</option>
                                    <option value="XXL" data-type="clothing">XXL</option>

                                    <option value="39" data-type="shoe">39</option>
                                    <option value="40" data-type="shoe">40</option>
                                    <option value="41" data-type="shoe">41</option>
                                    <option value="42" data-type="shoe">42</option>
                                    <option value="43" data-type="shoe">43</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label>Màu</label>
                                <input name="color" class="form-control" required>
                            </div>

                            <div class="form-group">
                                <label>Giá</label>
                                <input name="price" type="number" step="0.01"
                                       class="form-control" required>
                            </div>

                            <div class="form-group">
                                <label>Số lượng</label>
                                <input name="stock" type="number" min="0"
                                       class="form-control" required>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button class="btn btn-success">Lưu</button>
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/jquery@3.5.1/dist/jquery.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>