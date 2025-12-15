<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>在线聊天室</title>
    <link rel="stylesheet" href="css/chatroom.css">
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
<h1>
    欢迎，${username}
    <a href="logout" class="logout-link">退出登录</a>
</h1>

<div class="chat-area" id="chatArea">
    <c:forEach items="${messageList}" var="msg">
        <c:if test="${msg.type ne 'PRIVATE' or msg.sender eq username or msg.receiver eq username}">
            <div class="message ${fn:toLowerCase(msg.type)} ${msg.sender eq username ? 'self' : 'other'}">
                <span class="sender">${msg.sender}</span>
                <c:if test="${msg.type eq 'PRIVATE'}">
                    <span class="private-tag">私聊给${msg.receiver}</span>
                </c:if>
                <span class="time">${msg.time}</span>
                <span class="content">${msg.content}</span>
            </div>
        </c:if>
    </c:forEach>
</div>

<form action="chatroom" method="post" class="input-area" id="chatForm">
    <select name="receiver" class="receiver-select">
        <option value="all">全体成员</option>
        <c:forEach items="${onlineUsers}" var="user">
            <c:if test="${user ne username}">
                <option value="${user}">${user}</option>
            </c:if>
        </c:forEach>
    </select>
    <input type="text" name="content" placeholder="输入消息..." required>
    <button type="submit">发送</button>
</form>

<script>
    <%-- 用EL下标获取最后一个元素的ID（替换fn:last） --%>
    let lastMsgId = <c:choose>
            <c:when test="${not empty messageList}">${messageList[messageList.size() - 1].id}</c:when>
            <c:otherwise>0</c:otherwise>
        </c:choose>;
    const username = "${username}";

    $(function() {
        scrollToBottom();
        setInterval(fetchNewMessages, 3000);
    });

    function scrollToBottom() {
        const chatArea = $("#chatArea");
        chatArea.scrollTop(chatArea[0].scrollHeight);
    }

    function fetchNewMessages() {
        $.get("getNewMessages", {lastMsgId: lastMsgId}, function(data) {
            if (data.length > 0) {
                renderMessages(data);
                lastMsgId = data[data.length - 1].id;
                scrollToBottom();
            }
        }, "json").fail(function() {
            window.location.href = "login.jsp";
        });
    }

    function renderMessages(messages) {
        const chatArea = $("#chatArea");
        const fragment = document.createDocumentFragment();
        messages.forEach(msg => {
            if (msg.type === "PRIVATE" && msg.sender !== username && msg.receiver !== username) return;
            const isSelf = msg.sender === username;
            const typeClass = msg.type.toLowerCase();
            const privateTag = msg.type === "PRIVATE" ? `<span class="private-tag">私聊给${msg.receiver}</span>` : "";
            const msgDiv = document.createElement("div");
            msgDiv.className = `message ${typeClass} ${isSelf ? "self" : "other"}`;
            msgDiv.innerHTML = `
                    <span class="sender">${msg.sender}</span>
                    ${privateTag}
                    <span class="time">${msg.time}</span>
                    <span class="content">${msg.content}</span>
                `;
            fragment.appendChild(msgDiv);
        });
        chatArea.append(fragment);
    }
</script>
</body>
</html>