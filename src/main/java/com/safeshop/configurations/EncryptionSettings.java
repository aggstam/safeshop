package com.safeshop.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-encryption.properties")
public class EncryptionSettings {

    public static String SECRET;

    public static String ALGORITHM;

    @Value("${encryption.service.secret}")
    public void setSecret(String secret) {
        EncryptionSettings.SECRET = secret;
    }

    @Value("${encryption.service.algorithm}")
    public void setAlgorithm(String algorithm) {
        EncryptionSettings.ALGORITHM = algorithm;
    }

}
