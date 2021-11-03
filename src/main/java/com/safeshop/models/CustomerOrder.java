package com.safeshop.models;

import com.safeshop.services.EncryptionService;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable=false)
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable=false)
    private User buyer;

    @Column(nullable = false, columnDefinition = "varchar(32) default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private CustomerOrderStatus status;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Convert(converter = EncryptionService.class)
    private String address;

    @Column(nullable = false)
    @Convert(converter = EncryptionService.class)
    private String phone;

    @Column(nullable = false)
    private Date timestamp;

    public CustomerOrder() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public CustomerOrderStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerOrderStatus status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
