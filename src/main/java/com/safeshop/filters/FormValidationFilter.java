package com.safeshop.filters;

import com.safeshop.forms.CustomerOrderForm;
import com.safeshop.forms.ProductForm;
import com.safeshop.forms.RequestForm;
import com.safeshop.forms.UserForm;
import com.safeshop.models.User;
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
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Component
@Order(4)
public class FormValidationFilter extends MultipartFilter {

    private static final Logger logger = Logger.getLogger(FormValidationFilter.class.getName());

    @Autowired
    private CommonsMultipartResolver multipartResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // Form validity check.
        if (request.getMethod().equalsIgnoreCase("post")) {
            // Define request URI, to use the appropriate form validation.
            String uri = request.getRequestURI();
            if (uri.startsWith("/orders") || uri.startsWith("/products/delete/") || uri.startsWith("/users/delete/")) {
                RequestForm requestForm = new RequestForm();
                requestForm.setCaptchaResponse(request.getParameter("captchaResponse"));
                if (!requestForm.validityCheck()) {
                    printRequestInformation(request, request.getParameterMap());
                    response.sendRedirect("/forbidden");
                    return;
                }
            } else if (uri.startsWith("/product/placeOrder/")) {
                CustomerOrderForm customerOrderForm = new CustomerOrderForm();
                customerOrderForm.setQuantity(Integer.parseInt(request.getParameter("quantity")));
                customerOrderForm.setAddress(request.getParameter("address"));
                customerOrderForm.setPhone(request.getParameter("phone"));
                customerOrderForm.setCaptchaResponse(request.getParameter("captchaResponse"));
                if (!customerOrderForm.validityCheck()) {
                    printRequestInformation(request, request.getParameterMap());
                    response.sendRedirect("/forbidden");
                    return;
                }
            } else if (uri.startsWith("/products")) {
                if (!multipartResolver.isMultipart(request)) {
                    printRequestInformation(request, request.getParameterMap());
                    response.sendRedirect("/forbidden");
                    return;
                }
                ProductForm productForm = new ProductForm();
                productForm.setName(request.getParameterMap().get("name")[0]);
                productForm.setDescription(request.getParameterMap().get("description")[0]);
                productForm.setQuantity(Integer.parseInt(request.getParameterMap().get("quantity")[0]));
                productForm.setPrice(Integer.parseInt(request.getParameterMap().get("price")[0]));
                productForm.setCaptchaResponse(request.getParameterMap().get("captchaResponse")[0]);
                if (!productForm.validityCheck()) {
                    printRequestInformation(request, request.getParameterMap());
                    response.sendRedirect("/forbidden");
                    return;
                }
            } else if (uri.equals("/profile") || uri.startsWith("/users")) {
                UserForm userForm = new UserForm();
                userForm.setEmail(request.getParameter("email"));
                userForm.setName(request.getParameter("name"));
                userForm.setAddress(request.getParameter("address"));
                userForm.setPhone(request.getParameter("phone"));
                userForm.setCaptchaResponse(request.getParameter("captchaResponse"));
                if (!userForm.validityCheck()) {
                    printRequestInformation(request, request.getParameterMap());
                    response.sendRedirect("/forbidden");
                    return;
                }
            } else {
                printRequestInformation(request, request.getParameterMap());
                response.sendRedirect("/forbidden");
                return;
            }
        }
        // Continue filter chain.
        chain.doFilter(request, response);
    }

    private void printRequestInformation(HttpServletRequest request, Map<String, String[]> parameterMap) {
        String uri = request.getRequestURI();
        User user = (User) request.getSession().getAttribute("user");
        StringBuilder parametersMapStringBuilder = new StringBuilder();
        parameterMap.forEach((key, value) -> parametersMapStringBuilder.append("{").append(key).append(":").append(Arrays.toString(value)).append("}"));
        logger.info(String.format("[JSESSIONID - %s] [User %d tried to submit invalid form for URI: '%s'] [Parameters: %s]", request.getSession().getId(), user.getId(), uri, parametersMapStringBuilder.toString()));
    }

}
