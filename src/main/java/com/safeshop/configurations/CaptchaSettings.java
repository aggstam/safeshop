package com.safeshop.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-captcha.properties")
public class CaptchaSettings {

    public static String SITE;

    public static String SECRET;

    public static String VERIFYURL;

    @Value("${google.recaptcha.key.site}")
    public void setSite(String site) {
        CaptchaSettings.SITE = site;
    }

    @Value("${google.recaptcha.key.secret}")
    public void setSecret(String secret) {
        CaptchaSettings.SECRET = secret;
    }

    @Value("${google.recaptcha.key.verifyURL}")
    public void setVerifyURL(String verifyURL) {
        CaptchaSettings.VERIFYURL = verifyURL;
    }

}
