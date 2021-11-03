package com.safeshop.services;

import com.safeshop.configurations.CaptchaSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    // This method verifies provided captcha by executing a call to reCaptcha verification URL.
    public Boolean verifyCaptcha(String remoteIp, String response) {
        if(!responseSanityCheck(response)) {
            return false;
        }
        StringBuilder urlBuilder = new StringBuilder().append(CaptchaSettings.VERIFYURL)
                                                      .append("?secret=").append(CaptchaSettings.SECRET)
                                                      .append("&remoteip=").append(remoteIp)
                                                      .append("&response=").append(response);
        ResponseEntity<Map> recaptchaResponse = restTemplateBuilder.build().postForEntity(urlBuilder.toString(), new HashMap<>(), Map.class, new HashMap<>());
        if (recaptchaResponse.getBody() == null) {
            return false;
        }
        Map<String, Object> responseBody = recaptchaResponse.getBody();
        Boolean success = (Boolean) responseBody.get("success");
        if (success == null) {
            return false;
        }
        return success;
    }

    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

}
