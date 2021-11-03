<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Safe Shop</title>
    <link rel="stylesheet" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <script src="/static/js/jquery.slim.min.js"></script>
    <script src="/static/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="/static/css/utils.css">
    <script src="/static/js/album_search.js"></script>
    <script src="/static/js/redirect_button.js"></script>
</head>
<body>

    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="navbar-collapse collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link active" href="/home">Home</a></li>
                <c:if test="${user.seller}">
                    <li class="nav-item"><a class="nav-link" href="/products">Products</a></li>
                    <li class="nav-item"><a class="nav-link" href="/orders">Orders</a></li>
                </c:if>
                <c:if test="${user.admin}">
                    <li class="nav-item"><a class="nav-link" href="/users">Users</a></li>
                </c:if>
            </ul>
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a class="nav-link" href="/profile">Welcome ${fn:escapeXml(user.name)}! <i class="fa fa-fw fa-user"></i></a></li>
                <li class="nav-item"><a class="nav-link" href="/"><i class="fa fa-fw fa-sign-out"></i></a></li>
            </ul>
        </div>
    </nav>

    <main role="main" class="container mw-100">
        <div class="jumbotron text-center header-section">
            <div class="container">
                <h1>Products</h1>
                <input type="text" class="searchInput" placeholder="Search"/>
            </div>
        </div>

        <div class="album py-5">
            <div class="container">
                <div class="row">
                    <c:forEach  var="product" items="${products}">
                        <div class="col-md-4">
                            <div class="card mb-4 shadow-sm">
                                <img src="/static/images/products/${product.id}/${fn:escapeXml(product.image)}" width="100%" height="400">
                                <div class="card-body">
                                    <p class="card-text text-center">${fn:escapeXml(product.name)}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-sm btn-outline-secondary redirectButton" link="/product/${product.id}">View</button>
                                        </div>
                                        <small class="text-muted">
                                            Price: ${product.price} &euro;
                                        </small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    <c:forEach  var="unavailableProduct" items="${unavailableProducts}">
                        <div class="col-md-4">
                            <div class="card mb-4 shadow-sm">
                                <img src="/static/images/products/${unavailableProduct.id}/${fn:escapeXml(unavailableProduct.image)}" width="100%" height="400">
                                <div class="card-body">
                                    <p class="card-text text-center">${fn:escapeXml(unavailableProduct.name)}</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <small class="text-muted">Out of Stock</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>

</body>
</html>
