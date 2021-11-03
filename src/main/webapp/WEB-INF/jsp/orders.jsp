<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fn" uri = "http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <script src="/static/js/orders.js"></script>
</head>
<body>

    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
        <div class="navbar-collapse collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a class="nav-link" href="/home">Home</a></li>
                <c:if test="${user.seller}">
                    <li class="nav-item"><a class="nav-link" href="/products">Products</a></li>
                    <li class="nav-item"><a class="nav-link active" href="/orders">Orders</a></li>
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
                <h1>Orders</h1>
                <input type="text" class="searchInput" placeholder="Search"/>
            </div>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Product ID</th>
                        <th>Buyer</th>
                        <th>Email</th>
                        <th>Address</th>
                        <th>Phone</th>
                        <th>Date</th>
                        <th>Total</th>
                        <th>Status</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody id="customerOrdersTable">
                    <c:forEach  var="c" items="${customerOrders}">
                        <tr>
                            <td>${c.id}</td>
                            <td>${c.product.id}</td>
                            <td>${fn:escapeXml(c.buyer.name)}</td>
                            <td>${fn:escapeXml(c.buyer.email)}</td>
                            <td>${fn:escapeXml(c.address)}</td>
                            <td>${fn:escapeXml(c.phone)}</td>
                            <td>
                                <fmt:parseDate value="${c.timestamp}" pattern="yyyy-MM-dd HH:mm" var="timestamp"/>
                                <fmt:formatDate value="${timestamp}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td>${c.product.price * c.quantity} &euro;</td>
                            <td>${fn:escapeXml(c.status.text)}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${c.status == 'PENDING'}">
                                        <button type="button" class="btn btn-sm btn-outline-secondary completeOrderButton" data-toggle="modal" data-target="#completeOrderModal">Complete</button>
                                        <button type="button" class="btn btn-sm btn-outline-secondary cancel-button cancelOrderButton" data-toggle="modal" data-target="#cancelOrderModal">Cancel</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button type="button" class="btn btn-sm btn-outline-secondary" disabled>Complete</button>
                                        <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" disabled>Cancel</button>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div id="completeOrderModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form:form id="completeOrderForm" class="modal-content" modelAttribute="completeOrderForm" action="/orders/complete/" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Complete Order</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <p>Are you sure you want to complete the order?</p>
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="completeOrderCaptchaResponse"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Complete</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <div id="cancelOrderModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form:form id="cancelOrderForm" class="modal-content" modelAttribute="cancelOrderForm" action="/orders/cancel/" method="post">
                        <div class="modal-header">
                            <h5 class="modal-title">Cancel Order</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <p>Are you sure you want to cancel the order?</p>
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="cancelOrderCaptchaResponse"/>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-sm btn-outline-secondary cancel-button" data-dismiss="modal">Close</button>
                            <button type="submit" class="btn btn-sm btn-outline-secondary">Cancel</button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>

        <div id='recaptcha' class="g-recaptcha" data-sitekey="${siteKey}" data-callback="onCompleted" data-size="invisible"></div>
    </main>

</body>
</html>
