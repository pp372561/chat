<%@ page contentType="text/html;charset=UTF-8" language="java" import="bean.Message, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>在线聊天室</title>
    <link rel="stylesheet" type="text/css" href="css/chatroom.css">
</head>
<body>
<%
    String username = (String) session.getAttribute("username");
    List<Message> messageList = (List<Message>) application.getAttribute("messageList");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<h1>
    欢迎，<%= username %>
    <a href="logout" class="logout-link">退出登录</a>
</h1>

<div class="chat-area" id="chatArea">
    <%
        if (messageList != null) {
            for (Message msg : messageList) {
    %>
    <div class="message <%= msg.getSender().equals(username) ? "self" : "other" %>">
        <span class="sender"><%= msg.getSender() %></span>
        <span class="time"><%= msg.getTime() %></span>
        <span class="content"><%= msg.getContent() %></span>
    </div>
    <%
            }
        }
    %>
</div>

<form action="chatroom" method="post" class="input-area">
    <input type="text" name="content" placeholder="输入消息..." required>
    <button type="submit">发送</button>
</form>

<script>
    window.onload = function() {
        const chatArea = document.getElementById('chatArea');
        chatArea.scrollTop = chatArea.scrollHeight;
    }
</script>
</body>
</html>