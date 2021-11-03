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
    <script src="/static/js/product.js"></script>
</head>
<body>

    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="navbar-collapse collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="/home">Home</a></li>
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
                <h1>Product</h1>
            </div>
        </div>

        <div class="album py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-4">
                        <img src="/static/images/products/${product.id}/${fn:escapeXml(product.image)}" width="100%" height="400" class="listing-image">
                    </div>
                    <div class="details col-md-8">
                        <h2>Name: ${fn:escapeXml(product.name)}</h2>
                        <p><strong>Description: </strong>${fn:escapeXml(product.description)}</p>
                        <p><strong>Quantity: </strong>${product.quantity}</p>
                        <p><strong>Price: </strong>${product.price} &euro;</p>
                        <c:if test="${product.quantity > 0}">
                            <button type="button" class="btn btn-sm btn-outline-secondary" data-toggle="modal" data-target="#orderModal">Order</button>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${product.quantity > 0}">
            <div id="orderModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <form:form id="orderForm" class="modal-content" modelAttribute="customerOrderForm" action="/product/placeOrder/${product.id}" method="post" autocomplete="off">
                            <div class="modal-header">
                                <h5 class="modal-title">Order</h5>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            </div>
                            <div class="modal-body">
                                <div class="form-group">
                                    <label for="quantity" class="col-form-label">Quantity</label>
                                    <form:input path="quantity" type="number" class="form-control" id="quantity" min="1" max="${product.quantity}" step="1" placeholder="Enter Quantity" required="true"/>
                                </div>
                                <div class="form-group">
                                    <label for="address" class="col-form-label">Address</label>
                                    <form:input path="address" type="text" class="form-input" placeholder="Enter Address" id="address" minlength="2" maxlength="50" required="true"/>
                                </div>
                                <div class="form-group">
                                    <label for="phone" class="col-form-label">Phone</label>
                                    <form:input path="phone" type="tel" class="form-input" placeholder="Enter Phone (69********)" id="phone" pattern="69[0-9]{8}" required="true"/>
                                </div>
                                <div class="form-group">
                                    <form:hidden path="captchaResponse" id="captchaResponse"/>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-sm btn-outline-secondary">Order</button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

            <div id='recaptcha' class="g-recaptcha" data-sitekey="${siteKey}" data-callback="onCompleted" data-size="invisible"></div>
        </c:if>

    </main>

</body>
</html>
