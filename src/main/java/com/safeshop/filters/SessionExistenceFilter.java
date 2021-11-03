package com.safeshop.filters;

import com.safeshop.models.User;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class SessionExistenceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Filter does not apply to URIs "/", "/static/login/*" and "/loginSuccess".
        String uri = request.getRequestURI();
        if (!(uri.equals("/") || uri.startsWith("/static/login/") || uri.equals("/loginSuccess"))) {
            // Session existence check.
            User user = (User) request.getSession().getAttribute("user");
            if (user == null) {
                response.sendRedirect("/");
                return;
            }
        }
        // Continue filter chain.
        chain.doFilter(request, response);
    }

}
