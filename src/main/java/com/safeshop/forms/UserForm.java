package com.safeshop.forms;

import com.safeshop.models.User;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm extends RequestForm {

    @NotNull
    @Size(min=4, max=50)
    @Email
    private String email;

    @NotNull
    @Size(min=2, max=20)
    private String name;

    @NotNull
    @Size(min=2, max=50)
    private String address;

    @NotNull
    @Pattern(regexp="69[0-9]{8}")
    private String phone;

    private Boolean seller;

    private Boolean admin;

    public UserForm() {
        super();
    }

    public UserForm(User user) {
        super();
        this.email = user.getEmail();
        this.name = user.getName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
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

    @Override
    public Boolean validityCheck() {
        return (email != null && !StringUtils.isBlank(email) && EmailValidator.getInstance().isValid(email) && email.length() > 3 && email.length() < 51)
                && (name != null && !StringUtils.isBlank(name) && name.length() > 1 && name.length() < 21)
                && (address != null && !StringUtils.isBlank(address) && address.length() > 1 && address.length() < 51)
                && (phone != null && !StringUtils.isBlank(phone) && phone.matches("69[0-9]{8}"))
                && (captchaResponse != null && !StringUtils.isBlank(captchaResponse));
    }

}
