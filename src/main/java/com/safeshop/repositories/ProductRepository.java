package com.safeshop.repositories;

import com.safeshop.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> findAvailableProducts();

    @Query("SELECT p FROM Product p WHERE p.quantity <= 0")
    List<Product> findUnavailableProducts();

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId")
    List<Product> findSellerProducts(@Param("sellerId") Integer sellerId);

    @Query("SELECT p FROM Product p WHERE p.seller.id = :sellerId and p.id = :productId")
    Product findSellerProduct(@Param("sellerId") Integer sellerId, @Param("productId") Integer productId);

    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Product findProductById(@Param("productId") Integer productId);

}
