package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import bean.Message;
import bean.MessageType;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 短轮询获取新消息的Servlet
 */
@WebServlet("/getNewMessages")
public class GetNewMessagesServlet extends HttpServlet {
    // Jackson对象映射器（用于JSON序列化）
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // 未登录则返回空列表
        if (username == null) {
            objectMapper.writeValue(response.getWriter(), new CopyOnWriteArrayList<>());
            return;
        }

        // 获取最后一条消息的ID
        String lastMsgIdStr = request.getParameter("lastMsgId");
        long lastMsgId = 0;
        try {
            lastMsgId = Long.parseLong(lastMsgIdStr);
        } catch (Exception e) {}

        // 获取全局消息列表
        ServletContext context = getServletContext();
        List<Message> messageList = (List<Message>) context.getAttribute("messageList");
        if (messageList == null) {
            messageList = new CopyOnWriteArrayList<>();
            context.setAttribute("messageList", messageList);
        }

        // 过滤出最新的消息（增量获取）
        List<Message> newMessages = new CopyOnWriteArrayList<>();
        for (Message msg : messageList) {
            if (msg.getId() > lastMsgId) {
                // 过滤私聊消息：只显示自己发送或接收的
                if (msg.getType().equals(MessageType.PRIVATE)) {
                    if (msg.getSender().equals(username) || msg.getReceiver().equals(username)) {
                        newMessages.add(msg);
                    }
                } else {
                    newMessages.add(msg);
                }
            }
        }

        // 将新消息序列化为JSON返回
        objectMapper.writeValue(response.getWriter(), newMessages);
    }
}