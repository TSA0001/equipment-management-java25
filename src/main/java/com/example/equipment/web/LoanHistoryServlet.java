package com.example.equipment.web;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.equipment.model.UserAccount;
import com.example.equipment.service.LoanService;
import com.example.equipment.service.ServiceException;

@WebServlet(name = "LoanHistoryServlet", urlPatterns = {"/loans"})
public class LoanHistoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final LoanService loanService = new LoanService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 全履歴・全貸出中一覧は管理者専用
        HttpSession session = request.getSession(false);
        UserAccount loginUser = session == null ? null
                : (UserAccount) session.getAttribute(LoginServlet.SESSION_USER);
        if (loginUser == null || !"ADMIN".equals(loginUser.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "管理者権限が必要です。");
            return;
        }
        String mode = request.getParameter("mode");
        try {
            switch (mode != null ? mode : "") {
                case "active":
                    request.setAttribute("loans", loanService.findActiveLoans());
                    request.setAttribute("title", "貸出中一覧");
                    request.setAttribute("mode", "active");
                    break;
                default:
                    request.setAttribute("loans", loanService.findHistory());
                    request.setAttribute("title", "貸出履歴");
                    request.setAttribute("mode", "history");
                    break;
            }
            request.getRequestDispatcher("/WEB-INF/jsp/loanList.jsp").forward(request, response);
        } catch (ServiceException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/loanList.jsp").forward(request, response);
        }
    }
}
