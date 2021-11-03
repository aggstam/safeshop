package com.safeshop.models;

import com.safeshop.services.EncryptionService;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    @Convert(converter = EncryptionService.class)
    private String email;

    @Column
    @Convert(converter = EncryptionService.class)
    private String name;

    @Column
    @Convert(converter = EncryptionService.class)
    private String address;

    @Column
    @Convert(converter = EncryptionService.class)
    private String phone;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean seller;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean admin;

    @OneToMany(cascade={CascadeType.REMOVE}, mappedBy = "buyer", fetch = FetchType.LAZY)
    private List<CustomerOrder> customerOrders;

    public User() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getSeller() {
        return seller;
    }

    public void setSeller(Boolean seller) {
        this.seller = seller;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(List<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }

}
