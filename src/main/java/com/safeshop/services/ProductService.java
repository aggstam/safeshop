package com.safeshop.services;

import com.safeshop.models.Product;
import com.safeshop.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductService {

    @Resource
    private ProductRepository productRepository;

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }

    public List<Product> findAvailableProducts() {
        return productRepository.findAvailableProducts();
    }

    public List<Product> findUnavailableProducts() {
        return productRepository.findUnavailableProducts();
    }

    public List<Product> findSellerProducts(Integer sellerId) {
        return productRepository.findSellerProducts(sellerId);
    }

    public Product findSellerProduct(Integer sellerId, Integer productId) {
        return productRepository.findSellerProduct(sellerId, productId);
    }

    public Product findProductById(Integer productId) {
        return productRepository.findProductById(productId);
    }

}
