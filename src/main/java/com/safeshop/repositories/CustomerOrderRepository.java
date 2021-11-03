package com.safeshop.repositories;

import com.safeshop.models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {

    @Query("SELECT c FROM CustomerOrder c WHERE c.product.seller.id = :sellerId ORDER BY c.timestamp ASC")
    List<CustomerOrder> findSellerCustomerOrders(@Param("sellerId") Integer sellerId);

    @Query("SELECT c FROM CustomerOrder c WHERE c.product.seller.id = :sellerId and c.id = :orderId")
    CustomerOrder findSellerCustomerOrder(@Param("sellerId") Integer sellerId, @Param("orderId") Integer orderId);

}
