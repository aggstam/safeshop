<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Safe Shop</title>
    <link rel="stylesheet" href="/static/css/font-awesome.min.css">
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <script src="/static/js/jquery.slim.min.js"></script>
    <script src="/static/js/bootstrap.bundle.min.js"></script>
    <script src="/static/js/recaptcha/api.js?render=site-key" nonce="{NONCE}"></script>
    <link rel="stylesheet" href="/static/css/utils.css">
    <script src="/static/js/album_search.js"></script>
    <script src="/static/js/products.js"></script>
</head>
<body>

    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="navbar-collapse collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="/home">Home</a></li>
                <c:if test="${user.seller}">
                    <li class="nav-item"><a class="nav-link active" href="/products">Products</a></li>
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
                <h1>Products <button type="button" class="btn btn-sm btn-outline-secondary addButton" data-toggle="modal" data-target="#productModal">Add</button></h1>
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
                                    <p class="card-text"><b>Id: </b>${product.id}</p>
                                    <p class="card-text"><b>Name: </b>${fn:escapeXml(product.name)}</p>
                                    <p class="card-text"><b>Description: </b>${fn:escapeXml(product.description)}</p>
                                    <p class="card-text"><b>Quantity: </b>${product.quantity}</p>
                                    <p class="card-text"><b>Price: </b>${product.price} &euro;</p>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div class="btn-group">
                                            <button type="button" class="btn btn-sm btn-outline-secondary editButton" data-toggle="modal" data-target="#productModal">Edit</button>
                                            <button type="button" class="btn btn-sm btn-outline-secondary cancel-button deleteButton" data-toggle="modal" data-target="#deleteModal">Delete</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>

        <div id="productModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form:form id="productForm" class="modal-content" modelAttribute="productForm" action="/products" method="post" enctype="multipart/form-data" autocomplete="off">
                        <input id="_csrfToken" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <div class="modal-header">
                            <h5 id="modalTitle" class="modal-title">Add Product</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="name" class="col-form-label">Name</label>
                                <form:input path="name" type="text" class="form-input" placeholder="Enter Name" id="name" minlength="2" maxlength="20" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="description" class="col-form-label">Description</label>
                                <form:input path="description" type="text" class="form-input" placeholder="Enter Description" id="description" minlength="2" maxlength="150" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="quantity" class="col-form-label">Quantity</label>
                                <form:input path="quantity" type="number" class="form-input" placeholder="Enter Quantity" id="quantity" min="1" max="100" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="price" class="col-form-label">Price</label>
                                <form:input path="price" type="number" class="form-input" placeholder="Enter Price" id="price" min="0" step="1" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="image" class="col-form-label">Image</label>
                                <form:input path="file" type="file" class="form-input" id="image" size="5" accept=".png,.jpg,.jpeg" />
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="productFormCaptchaResponse"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" data-dismiss="modal">Close</button>
                            <button id="submitButton" type="submit" class="btn btn-sm btn-outline-secondary">Add</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <div id="deleteModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form:form id="deleteForm" class="modal-content" modelAttribute="deleteProductForm" action="/products/delete/" method="post" autocomplete="off">
                        <div class="modal-header">
                            <h5 class="modal-title">Delete Product</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <p>Are you sure you want to permanently remove the product?</p>
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="deleteProductFormCaptchaResponse"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Delete</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <div id='recaptcha' class="g-recaptcha" data-sitekey="${siteKey}" data-callback="onCompleted" data-size="invisible"></div>
    </main>

</body>
</html>
