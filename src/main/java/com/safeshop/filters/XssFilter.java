package com.safeshop.filters;

import com.safeshop.xss.XssMultipartRequestWrapper;
import com.safeshop.xss.XssRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class XssFilter extends MultipartFilter {

    @Autowired
    private CommonsMultipartResolver multipartResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Define request type, to use the appropriate Wrapper.
//        if (request.getMethod().equalsIgnoreCase("post") && multipartResolver.isMultipart(request)) {
//            request = multipartResolver.resolveMultipart(request);
//            request = new XssMultipartRequestWrapper(request);
//        } else {
//            request = new XssRequestWrapper(request);
//        }
        // Continue filter chain.
        chain.doFilter(request, response);
    }

}
