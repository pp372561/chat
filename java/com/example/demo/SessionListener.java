package com.example.demo;

import bean.Message;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Session监听器：处理用户异常退出
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String username = (String) session.getAttribute("username");

        // 用户名不存在则直接返回
        if (username == null) {
            return;
        }

        // 判断是否是主动退出（有标记则跳过）
        Boolean isInitiativeLogout = (Boolean) session.getAttribute("isInitiativeLogout");
        if (isInitiativeLogout != null && isInitiativeLogout) {
            return;
        }

        // 异常退出处理：移除在线用户+添加系统通知
        ServletContext context = session.getServletContext();
        Set<String> onlineUsers = (Set<String>) context.getAttribute("onlineUsers");
        List<Message> messageList = (List<Message>) context.getAttribute("messageList");

        // 移除在线用户
        if (onlineUsers != null) {
            onlineUsers.remove(username);
        }

        // 添加异常退出通知
        if (messageList == null) {
            messageList = new CopyOnWriteArrayList<>();
            context.setAttribute("messageList", messageList);
        }
        messageList.add(new Message(username + "异常退出了聊天室"));
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 空实现：无需处理Session创建
    }
}