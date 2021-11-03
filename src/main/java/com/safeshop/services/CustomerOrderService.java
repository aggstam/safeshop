package com.safeshop.services;

import com.safeshop.models.CustomerOrder;
import com.safeshop.repositories.CustomerOrderRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerOrderService {

    @Resource
    private CustomerOrderRepository customerOrderRepository;

    public CustomerOrder save(CustomerOrder customerOrder) {
        return customerOrderRepository.save(customerOrder);
    }

    public List<CustomerOrder> findSellerCustomerOrders(Integer sellerId) {
        return customerOrderRepository.findSellerCustomerOrders(sellerId);
    }

    public CustomerOrder findSellerCustomerOrder(Integer sellerId, Integer orderId) {
        return customerOrderRepository.findSellerCustomerOrder(sellerId, orderId);
    }

}
