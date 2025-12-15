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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/chatroom")
public class ChatRoomServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();
        if (context.getAttribute("messageList") == null) {
            context.setAttribute("messageList", new ArrayList<Message>());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 将消息列表和用户名传递给JSP
        request.setAttribute("username", username);
        request.setAttribute("messageList", getServletContext().getAttribute("messageList"));
        request.getRequestDispatcher("chatroom.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String content = request.getParameter("content");
        if (content != null && !content.trim().isEmpty()) {
            Message message = new Message(username, content.trim());
            List<Message> messageList = (List<Message>) getServletContext().getAttribute("messageList");
            messageList.add(message);
        }

        response.sendRedirect("chatroom");
    }
}