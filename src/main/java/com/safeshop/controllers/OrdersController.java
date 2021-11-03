package com.safeshop.controllers;

import com.safeshop.configurations.CaptchaSettings;
import com.safeshop.forms.CancelOrderForm;
import com.safeshop.forms.CompleteOrderForm;
import com.safeshop.models.CustomerOrder;
import com.safeshop.models.CustomerOrderStatus;
import com.safeshop.models.Product;
import com.safeshop.models.User;
import com.safeshop.services.CustomerOrderService;
import com.safeshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class OrdersController {

    private static final Logger logger = Logger.getLogger(OrdersController.class.getName());

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ProductService productService;

    // This method creates the orders page view, accessible by users with the appropriate right.
    @GetMapping(value="/orders")
    public ModelAndView getOrders(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            List<CustomerOrder> customerOrders = customerOrderService.findSellerCustomerOrders(((User) request.getSession().getAttribute("user")).getId());
            ModelAndView mav = new ModelAndView("orders");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("customerOrders", customerOrders);
            mav.addObject("completeOrderForm", new CompleteOrderForm());
            mav.addObject("cancelOrderForm", new CancelOrderForm());
            mav.addObject("siteKey", CaptchaSettings.SITE);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to complete an order.
    @PostMapping(value="/orders/complete/{id}")
    public ModelAndView completeOrder(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute CompleteOrderForm completeOrderForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Order existence check.
            CustomerOrder customerOrder = customerOrderService.findSellerCustomerOrder(((User) request.getSession().getAttribute("user")).getId(), id);
            if (customerOrder == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Complete execution.
            customerOrder.setStatus(CustomerOrderStatus.COMPLETED);
            customerOrderService.save(customerOrder);
            Product product = customerOrder.getProduct();
            product.setQuantity(product.getQuantity() - customerOrder.getQuantity());
            productService.save(product);
            return new ModelAndView("redirect:/orders");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to cancel an order.
    @PostMapping(value="/orders/cancel/{id}")
    public ModelAndView cancelOrder(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute CancelOrderForm cancelOrderForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Order existence check.
            CustomerOrder customerOrder = customerOrderService.findSellerCustomerOrder(((User) request.getSession().getAttribute("user")).getId(), id);
            if (customerOrder == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Complete execution.
            customerOrder.setStatus(CustomerOrderStatus.CANCELLED);
            customerOrderService.save(customerOrder);
            return new ModelAndView("redirect:/orders");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
