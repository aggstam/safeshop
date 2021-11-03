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
    <link rel="stylesheet" href="/static/css/profile.css">
    <script src="/static/js/profile.js"></script>
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
                <li class="nav-item"><a class="nav-link active" href="/profile">Welcome ${fn:escapeXml(user.name)}! <i class="fa fa-fw fa-user"></i></a></li>
                <li class="nav-item"><a class="nav-link" href="/"><i class="fa fa-fw fa-sign-out"></i></a></li>
            </ul>
        </div>
    </nav>

    <main role="main" class="container">
        <form:form id="profileForm" class="modal-content animate" modelAttribute="userForm" action="/profile" method="post" autocomplete="off">
            <div class="img-container"><img src="/static/images/profile_picture.png" alt="Avatar" class="avatar"></div>
            <div class="info-container">
                <p><b>Update your account information:</b></p>
                <label><b>Email</b></label>
                <form:input path="email" type="email" name="email" minlength="4" maxlength="50" readonly="true"/>
                <label><b>Name</b></label>
                <form:input path="name" type="text" placeholder="Enter Name" name="name" minlength="2" maxlength="20" required="true"/>
                <label><b>Address</b></label>
                <form:input path="address" type="text" placeholder="Enter Address" name="adress" minlength="2" maxlength="50" required="true"/>
                <label><b>Phone</b></label>
                <form:input path="phone" type="tel" placeholder="Enter Phone (69********)" name="phone" pattern="69[0-9]{8}" required="true"/>
                <form:hidden path="captchaResponse" id="captchaResponse"/>
                <button type="submit" class="form-button">Save changes</button>
            </div>
        </form:form>

        <div id='recaptcha' class="g-recaptcha" data-sitekey="${siteKey}" data-callback="onCompleted" data-size="invisible"></div>
    </main>

</body>
</html>
