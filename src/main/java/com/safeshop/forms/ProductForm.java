package com.safeshop.forms;

import com.safeshop.services.FilesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProductForm extends RequestForm {

    @NotNull
    @Size(min=2, max=20)
    private String name;

    @NotNull
    @Size(min=2, max=150)
    private String description;

    @NotNull
    @Min(0)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Integer price;

    private MultipartFile file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public Boolean validityCheck() {
        return (name != null && !StringUtils.isBlank(name) && name.length() > 1 && name.length() < 21)
                && (description != null && !StringUtils.isBlank(description) && description.length() > 1 && description.length() < 151)
                && (quantity != null && quantity > -1)
                && (price != null && price > -1)
                && (captchaResponse != null && !StringUtils.isBlank(captchaResponse))
                && FilesService.checkFileMimeType(file);
    }

}
