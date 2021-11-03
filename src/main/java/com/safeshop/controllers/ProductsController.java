package com.safeshop.controllers;

import com.safeshop.configurations.CaptchaSettings;
import com.safeshop.forms.DeleteProductForm;
import com.safeshop.forms.ProductForm;
import com.safeshop.models.Product;
import com.safeshop.models.User;
import com.safeshop.services.FilesService;
import com.safeshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class ProductsController {

    private static final Logger logger = Logger.getLogger(ProductsController.class.getName());

    @Autowired
    private ProductService productService;

    @Autowired
    private ServletContext context;

    // This method creates the products page view.
    @GetMapping(value="/products")
    public ModelAndView getProducts(HttpServletRequest request) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            List<Product> products = productService.findSellerProducts(((User) request.getSession().getAttribute("user")).getId());
            ModelAndView mav = new ModelAndView("products");
            mav.addObject("user", request.getSession().getAttribute("user"));
            mav.addObject("products", products);
            mav.addObject("productForm", new ProductForm());
            mav.addObject("deleteProductForm", new DeleteProductForm());
            mav.addObject("siteKey", CaptchaSettings.SITE);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to create a new product.
    @PostMapping(value="/products")
    public ModelAndView addProduct(HttpServletRequest request, @Valid @ModelAttribute ProductForm productForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Product creation.
            Product product = new Product();
            product.setSeller((User) request.getSession().getAttribute("user"));
            product.setName(productForm.getName());
            product.setDescription(productForm.getDescription());
            product.setQuantity(productForm.getQuantity());
            product.setPrice(productForm.getPrice());
            productService.save(product);
            FilesService.setProductImageFile(context, product, productForm.getFile());
            productService.save(product);
            return new ModelAndView("redirect:/products");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to edit the information of a product.
    @PostMapping(value="/products/{id}")
    public ModelAndView editProduct(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute ProductForm productForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Product existence check.
            Product product = productService.findSellerProduct(((User) request.getSession().getAttribute("user")).getId(), id);
            if (product == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Edit execution.
            product.setName(productForm.getName());
            product.setDescription(productForm.getDescription());
            product.setQuantity(productForm.getQuantity());
            product.setPrice(productForm.getPrice());
            FilesService.setProductImageFile(context, product, productForm.getFile());
            productService.save(product);
            return new ModelAndView("redirect:/products");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

    // This method enables users with appropriate right to delete a product.
    @PostMapping(value="/products/delete/{id}")
    public ModelAndView deleteProduct(HttpServletRequest request, @PathVariable Integer id, @Valid @ModelAttribute DeleteProductForm deleteProductForm) {
        try {
            logger.info(String.format("[JSESSIONID - %s] [Method requested.]", request.getSession().getId()));
            // Product existence check.
            Product product = productService.findSellerProduct(((User) request.getSession().getAttribute("user")).getId(), id);
            if (product == null) {
                return new ModelAndView("redirect:/notFound");
            }
            // Delete execution.
            FilesService.deleteProductImageFile(context, product.getId());
            productService.delete(product);
            return new ModelAndView("redirect:/products");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(String.format("[JSESSIONID - %s] [Exception: %s]", request.getSession().getId(), e.getMessage()));
            return new ModelAndView("redirect:/error");
        }
    }

}
