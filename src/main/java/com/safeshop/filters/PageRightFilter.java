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
import java.util.logging.Logger;

@Component
@Order(3)
public class PageRightFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(PageRightFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // User right check.
        String uri = request.getRequestURI();
        User user = (User) request.getSession().getAttribute("user");
        if (((uri.startsWith("/orders") || uri.startsWith("/products")) && !user.getSeller()) || (uri.startsWith("/users") && !user.getAdmin())) {
            logger.info(String.format("[JSESSIONID - %s] [User %d tried to access '%s' without the proper right.]", request.getSession().getId(), user.getId(), uri));
            response.sendRedirect("/notFound");
            return;
        }
        // Continue filter chain.
        chain.doFilter(request, response);
    }

}
