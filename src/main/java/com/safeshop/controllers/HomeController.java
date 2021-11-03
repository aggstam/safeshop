package com.safeshop.controllers;

import com.safeshop.models.Product;
import com.safeshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class HomeController {

    private static final Logger logger = Logger.getLogger(HomeController.class.getName());

    @Autowired
    private ProductService productService;

    // This method creates the home page view.
    @GetMapping(value="/home")
    public ModelAndView getHome(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            List<Product> products = productService.findAvailableProducts();
            List<Product> unavailableProducts = productService.findUnavailableProducts();
            ModelAndView mav = new ModelAndView("home");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("products", products);
            mav.addObject("unavailableProducts", unavailableProducts);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
