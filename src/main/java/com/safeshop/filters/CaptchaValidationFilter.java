package com.safeshop.filters;

import com.safeshop.models.User;
import com.safeshop.services.CaptchaService;
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
import java.util.Map;
import java.util.logging.Logger;

@Component
@Order(5)
public class CaptchaValidationFilter extends MultipartFilter {

    private static final Logger logger = Logger.getLogger(CaptchaValidationFilter.class.getName());

    @Autowired
    private CommonsMultipartResolver multipartResolver;

    @Autowired
    private CaptchaService captchaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Captcha validity check.
        if (request.getMethod().equalsIgnoreCase("post")) {
            String captchaResponse = request.getParameter("captchaResponse");
            if (multipartResolver.isMultipart(request)) {
                Map<String, String[]> multipartRequestParams = request.getParameterMap();
                captchaResponse = multipartRequestParams.get("captchaResponse")[0];
            }
            if (!captchaService.verifyCaptcha(request.getRemoteAddr(), captchaResponse)) {
                String uri = request.getRequestURI();
                User user = (User) request.getSession().getAttribute("user");
                logger.info(String.format("[JSESSIONID - %s] [User %d submitted a valid form for URI '%s' with an invalid Captcha.]", request.getSession().getId(), user.getId(), uri));
                response.sendRedirect("/forbidden");
                return;
            }
        }
        // Continue filter chain.
        chain.doFilter(request, response);
    }

}
