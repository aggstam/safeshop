package com.safeshop.forms;

import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;

public class RequestForm {

    @NotNull
    String captchaResponse;

    public RequestForm() {}

    public String getCaptchaResponse() {
        return captchaResponse;
    }

    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }

    public Boolean validityCheck() {
        return (captchaResponse != null && !StringUtils.isBlank(captchaResponse));
    }

}
