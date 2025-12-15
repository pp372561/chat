<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>聊天室登录</title>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<div class="login-container">
    <h2>在线聊天室</h2>
    <!-- 显示错误信息（EL表达式） -->
    <c:if test="${not empty error}">
        <div class="error-message">${error}</div>
    </c:if>
    <form action="login" method="post">
        <div class="form-group">
            <input type="text" name="username" placeholder="请输入用户名" required>
        </div>
        <button type="submit">进入聊天</button>
    </form>
</div>
</body>
</html>