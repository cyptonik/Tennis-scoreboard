package org.tennis.scoreboard.filter;

import org.tennis.scoreboard.exception.AppException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AppExceptionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        try {
            chain.doFilter(request, response);
        } catch (AppException e) {
            sendError(e.getStatus(), e.getMessage(), resp, req);
        } catch (IllegalArgumentException e) {
            sendError(400, e.getMessage(), resp, req);
        }
    }

    private void sendError(int status, String message, HttpServletResponse resp, HttpServletRequest req) throws IOException {
        String url = req.getHeader("Referer");
        req.getSession().setAttribute("errorMessage", message);
        resp.setStatus(status);
        resp.sendRedirect(url != null ? url : "/tennis_app/");
    }
}
