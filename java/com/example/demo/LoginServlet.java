package com.example.demo;

import bean.Message;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");

        // 验证用户名非空
        if (username == null || username.trim().isEmpty()) {
            request.setAttribute("error", "用户名不能为空");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // 存储用户到Session
        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        // 添加用户到在线列表
        ServletContext context = getServletContext();
        Set<String> onlineUsers = (Set<String>) context.getAttribute("onlineUsers");
        if (onlineUsers == null) {
            onlineUsers = ConcurrentHashMap.newKeySet();
            context.setAttribute("onlineUsers", onlineUsers);
        }
        onlineUsers.add(username);

        // ========== 新增：添加用户进入的系统通知 ==========
        List<Message> messageList = (List<Message>) context.getAttribute("messageList");
        if (messageList == null) {
            messageList = new CopyOnWriteArrayList<>();
            context.setAttribute("messageList", messageList);
        }
        messageList.add(new Message(username + "进入了聊天室")); // 系统消息

        // 重定向到聊天室
        response.sendRedirect("chatroom");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}