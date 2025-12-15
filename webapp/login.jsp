<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>聊天室登录</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
<div class="login-container">
    <h2>在线聊天室</h2>
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
    <div class="error-message"><%= error %></div>
    <% } %>
    <form action="login" method="post">
        <div class="form-group">
            <input type="text" name="username" placeholder="请输入用户名" required>
        </div>
        <button type="submit">进入聊天</button>
    </form>
</div>
</body>
</html>