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
    <script src="/static/js/users.js"></script>
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
                    <li class="nav-item"><a class="nav-link active" href="/users">Users</a></li>
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
                <h1>User Management  <button type="button" class="btn btn-sm btn-outline-secondary addButton" data-toggle="modal" data-target="#userModal">Add</button></h1>
                <input type="text" class="searchInput" placeholder="Search"/>
            </div>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Email</th>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Phone</th>
                        <th>Seller</th>
                        <th>Admin</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody id="usersTable">
                    <c:forEach  var="u" items="${users}">
                        <tr>
                            <td>${u.id}</td>
                            <td>${fn:escapeXml(u.email)}</td>
                            <td>${fn:escapeXml(u.name)}</td>
                            <td>${fn:escapeXml(u.address)}</td>
                            <td>${fn:escapeXml(u.phone)}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.seller}">
                                        <i class="fa fa-fw fa-check"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-fw fa-times"></i>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.admin}">
                                        <i class="fa fa-fw fa-check"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-fw fa-times"></i>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <button type="button" class="btn btn-sm btn-outline-secondary editButton" data-toggle="modal" data-target="#userModal">Edit</button>
                                <button type="button" class="btn btn-sm btn-outline-secondary cancel-button deleteButton" data-toggle="modal" data-target="#deleteModal">Delete</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div id="userModal" class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form:form id="userForm" class="modal-content" modelAttribute="userForm" action="/users" method="post" autocomplete="off">
                        <div class="modal-header">
                            <h5 id="modalTitle" class="modal-title">Add User</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="email" class="col-form-label">Email</label>
                                <form:input path="email" type="email" class="form-input" placeholder="Enter Email" id="email" minlength="4" maxlength="50" required="true"/>
                            </div>
                            <div class="form-group">
                                <label for="name" class="col-form-label">Name</label>
                                <form:input path="name" type="text" class="form-input" placeholder="Enter Name" id="name" minlength="2" maxlength="20" required="true"/>
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
                                <label for="seller" class="col-form-label">Seller</label>
                                <form:checkbox path="seller" class="form-input custom-checkbox" id="seller"/>
                            </div>
                            <div class="form-group">
                                <label for="admin" class="col-form-label">Admin</label>
                                <form:checkbox path="admin" class="form-input custom-checkbox" id="admin"/>
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="userFormCaptchaResponse"/>
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
                    <form:form id="deleteForm" class="modal-content" modelAttribute="deleteUserForm" action="/users/delete/" method="post" autocomplete="off">
                        <div class="modal-header">
                            <h5 class="modal-title">Delete User</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <p>Are you sure you want to permanently remove the user?</p>
                            </div>
                            <div class="form-group">
                                <form:hidden path="captchaResponse" id="deleteUserFormCaptchaResponse"/>
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
