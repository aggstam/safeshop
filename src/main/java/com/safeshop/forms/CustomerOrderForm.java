package com.safeshop.forms;

import com.safeshop.models.User;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerOrderForm extends RequestForm {

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Size(min=2, max=50)
    private String address;

    @NotNull
    @Pattern(regexp="69[0-9]{8}")
    private String phone;

    public CustomerOrderForm() {
        super();
    }

    public CustomerOrderForm(User user) {
        super();
        this.address = user.getAddress();
        this.phone =  user.getPhone();
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

    @Override
    public Boolean validityCheck() {
        return (quantity != null && quantity > 0)
                && (address != null && !StringUtils.isBlank(address) && address.length() > 1 && address.length() < 51)
                && (phone != null && !StringUtils.isBlank(phone) && phone.matches("69[0-9]{8}"))
                && (captchaResponse != null && !StringUtils.isBlank(captchaResponse));
    }

}
