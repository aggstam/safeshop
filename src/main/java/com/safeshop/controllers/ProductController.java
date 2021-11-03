package com.safeshop.controllers;

import com.safeshop.configurations.CaptchaSettings;
import com.safeshop.forms.CustomerOrderForm;
import com.safeshop.models.CustomerOrderStatus;
import com.safeshop.models.CustomerOrder;
import com.safeshop.models.Product;
import com.safeshop.models.User;
import com.safeshop.services.CustomerOrderService;
import com.safeshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.logging.Logger;

@Controller
public class ProductController {

    private static final Logger logger = Logger.getLogger(ProductController.class.getName());

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerOrderService customerOrderService;

    // This method creates the product page view.
    @GetMapping(value="/product/{id}")
    public ModelAndView getProduct(HttpServletRequest request, @PathVariable Integer id) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Product existence check.
            Product product = productService.findProductById(id);
            if (product == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // View creation.
            ModelAndView mav = new ModelAndView("product");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("product", product);
            if (product.getQuantity() > 0) {
                mav.addObject("customerOrderForm", new CustomerOrderForm((User) request.getSession().getAttribute("user")));
                mav.addObject("siteKey", CaptchaSettings.SITE);
            }
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users to place an order for a product.
    @PostMapping(value="/product/placeOrder/{id}")
    public ModelAndView placeOrder(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute CustomerOrderForm customerOrderForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Product existence check.
            Product product = productService.findProductById(id);
            if (product == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Order execution.
            if (product.getQuantity() > 0 && product.getQuantity() >= customerOrderForm.getQuantity()) {
                CustomerOrder customerOrder = new CustomerOrder();
                customerOrder.setProduct(product);
                customerOrder.setBuyer((User) request.getSession().getAttribute("user"));
                customerOrder.setStatus(CustomerOrderStatus.PENDING);
                customerOrder.setQuantity(customerOrderForm.getQuantity());
                customerOrder.setAddress(customerOrderForm.getAddress());
                customerOrder.setPhone(customerOrderForm.getPhone());
                customerOrder.setTimestamp(new Date());
                customerOrderService.save(customerOrder);
            }
            return new ModelAndView("redirect:/product/" + id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
