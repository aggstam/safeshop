<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Safe Shop Login</title>
    <link rel="stylesheet" href="/static/login/font-awesome.min.css">
    <link rel="stylesheet" href="/static/login/login.css">
</head>
<body>

    <main role="main" class="container">
        <div class="img-container">
            <img src="/static/login/safeshop_logo.png" class="login-img">
        </div>
        <div class="info-container">
            <c:forEach  var="provider" items="${providers}">
                <p>You can login using your ${provider.key} account by clicking here:
                    <a href="${provider.value}">
                        <c:choose>
                            <c:when test="${provider.key == 'Google'}">
                                <i class="google-icon"></i>
                            </c:when>
                            <c:otherwise>
                                ${provider.key}
                            </c:otherwise>
                        </c:choose>
                    </a>
                </p>
            </c:forEach>
            <p class="error-message">${errorMessage}</p>
        </div>
    </main>

</body>
</html>
