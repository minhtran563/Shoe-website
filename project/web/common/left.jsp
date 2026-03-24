<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* ===== TỔNG THỂ ===== */
    .sidebar-container {
        font-family: 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
    }

    /* ===== CATEGORY (DANH MỤC) ===== */
    .category-card {
        border: none;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 20px rgba(0,0,0,0.05);
        transition: transform 0.3s ease;
    }

    .category-card .card-header {
        background: #1a1a1a; /* Màu tối sang trọng */
        color: #fff;
        font-weight: 600;
        font-size: 16px;
        padding: 15px;
        border: none;
        display: flex;
        align-items: center;
    }

    .category-card .list-group-item {
        border-left: none;
        border-right: none;
        border-color: #f0f0f0;
        padding: 12px 20px;
        transition: all 0.2s ease;
    }

    .category-card .list-group-item a {
        font-size: 15px;
        font-weight: 500;
        color: #444;
        text-decoration: none;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .category-card .list-group-item:hover {
        background-color: #f8f9fa;
        padding-left: 25px; /* Hiệu ứng đẩy sang phải */
    }

    .category-card .list-group-item:hover a {
        color: #007bff;
    }

    /* ===== PRODUCT CARD (SẢN PHẨM) ===== */
    .product-card {
        border: none;
        border-radius: 15px;
        box-shadow: 0 10px 30px rgba(0,0,0,0.08);
        overflow: hidden;
        background: #fff;
        margin-top: 25px;
    }

    .product-card .card-header {
        background: linear-gradient(135deg, #ff416c, #ff4b2b); /* Gradient rực rỡ */
        color: white;
        font-weight: 700;
        text-align: center;
        padding: 12px;
        border: none;
        text-transform: uppercase;
        font-size: 14px;
        letter-spacing: 1px;
    }

    /* Ảnh wrapper */
    .image-wrapper {
        position: relative;
        padding: 15px;
        background: #fdfdfd;
        overflow: hidden;
    }

    .image-wrapper img {
        width: 100%;
        border-radius: 10px;
        transition: all 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }

    .product-card:hover img {
        transform: scale(1.05);
    }

    /* Ribbon "NEW" */
    .ribbon {
        position: absolute;
        top: 20px;
        left: -35px;
        background: #ffc107;
        color: #000;
        padding: 5px 40px;
        font-size: 11px;
        font-weight: 800;
        transform: rotate(-45deg);
        z-index: 10;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
    }

    /* Tên sản phẩm & Badge Bán chạy */
    .product-info {
        padding: 20px 15px;
    }

    .product-link {
        color: #222;
        text-decoration: none;
        font-weight: 700;
        font-size: 18px;
        display: block;
        margin-bottom: 8px;
        transition: color 0.2s;
    }

    .product-link:hover {
        color: #ff416c;
        text-decoration: none;
    }

    .badge-hot {
        background-color: #e74c3c;
        color: white;
        font-size: 11px;
        padding: 3px 8px;
        border-radius: 20px;
        text-transform: uppercase;
        font-weight: bold;
        display: inline-block;
        vertical-align: middle;
        margin-left: 5px;
        animation: pulse 2s infinite;
    }

    @keyframes pulse {
        0% {
            transform: scale(1);
        }
        50% {
            transform: scale(1.05);
        }
        100% {
            transform: scale(1);
        }
    }
</style>

<div class="col-sm-3 sidebar-container">

    <div class="card mb-4 category-card">
        <div class="card-header">
            <i class="fa fa-th-large mr-2"></i> Categories
        </div>
        <ul class="list-group list-group-flush">
            <c:forEach items="${categories}" var="c">
                <li class="list-group-item">
                    <a href="home?cid=${c.categoryID}">
                        ${c.categoryName}
                        <i class="fa fa-angle-right" style="opacity: 0.5;"></i>
                    </a>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div class="card product-card">
        <div class="card-header">
            New Product
        </div>
        <div class="image-wrapper">
            <div class="ribbon">NEW</div>
            <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmPJ7i4x3Wvs0RbYPvE28BAxn7F8DeXzPNSg&s"
                 alt="Giày Nike Air"
                 class="img-fluid">
        </div>
        <div class="product-info text-center">
            <a href="#" class="product-link">
                Giày Nike Air 
                <span class="badge-hot">Bán chạy</span>
            </a>
            <div style="color: #888; font-size: 14px;">Thiết kế thời thượng 2026</div>
        </div>
    </div>

</div>