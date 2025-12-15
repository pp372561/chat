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
 * 退出登录处理Servlet
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username != null) {
            // 从在线用户列表移除
            ServletContext context = getServletContext();
            Set<String> onlineUsers = (Set<String>) context.getAttribute("onlineUsers");
            if (onlineUsers != null) {
                onlineUsers.remove(username);
            }

            // 添加退出通知
            List<Message> messageList = (List<Message>) context.getAttribute("messageList");
            if (messageList == null) {
                messageList = new CopyOnWriteArrayList<>();
                context.setAttribute("messageList", messageList);
            }
            messageList.add(new Message(username + "退出了聊天室"));

            // 标记主动退出（避免SessionListener误判为异常退出）
            session.setAttribute("isInitiativeLogout", true);
        }

        // 销毁Session
        session.invalidate();
        // 重定向到登录页
        response.sendRedirect("login.jsp");
    }
}