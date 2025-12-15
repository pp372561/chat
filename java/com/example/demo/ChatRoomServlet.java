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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 聊天室核心Servlet：处理页面转发和消息发送
 */
@WebServlet("/chatroom")
public class ChatRoomServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        // 初始化消息列表（线程安全）
        if (context.getAttribute("messageList") == null) {
            context.setAttribute("messageList", new CopyOnWriteArrayList<Message>());
        }
        // 初始化在线用户列表（线程安全）
        if (context.getAttribute("onlineUsers") == null) {
            context.setAttribute("onlineUsers", java.util.concurrent.ConcurrentHashMap.newKeySet());
        }
    }

    // 处理GET请求：转发到聊天室JSP
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // 未登录则重定向到登录页
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 传递数据到JSP（供EL表达式使用）
        request.setAttribute("username", username);
        request.setAttribute("messageList", getServletContext().getAttribute("messageList"));
        request.setAttribute("onlineUsers", getServletContext().getAttribute("onlineUsers"));

        // 转发到chatroom.jsp
        request.getRequestDispatcher("chatroom.jsp").forward(request, response);
    }

    // 处理POST请求：发送消息
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // 未登录则重定向到登录页
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 获取请求参数
        String content = request.getParameter("content");
        String receiver = request.getParameter("receiver");

        // 验证消息内容非空
        if (content != null && !content.trim().isEmpty()) {
            Message message;
            // 判断是私聊还是群消息
            if (receiver != null && !receiver.equals("all") && !receiver.trim().isEmpty()) {
                message = new Message(username, content.trim(), receiver);
            } else {
                message = new Message(username, content.trim());
            }

            // 添加消息到全局列表
            List<Message> messageList = (List<Message>) getServletContext().getAttribute("messageList");
            messageList.add(message);

            // 限制消息列表最大长度为200条
            if (messageList.size() > 200) {
                List<Message> newList = new CopyOnWriteArrayList<>(messageList.subList(messageList.size() - 200, messageList.size()));
                getServletContext().setAttribute("messageList", newList);
            }
        }

        // 重定向回聊天室（避免表单重复提交）
        response.sendRedirect("chatroom");
    }
}