package com.safeshop.services;

import com.safeshop.configurations.EncryptionSettings;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.Key;
import java.util.Base64;

@Converter
public class EncryptionService implements AttributeConverter<String, String> {

    private Key key;
    private Cipher cipher;

    public EncryptionService() {}

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            if (!initialized() || attribute == null || StringUtils.isBlank(attribute)) {
                return null;
            }
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String convertToEntityAttribute(String attribute) {
        try {
            if (!initialized() || attribute == null || StringUtils.isBlank(attribute)) {
                return null;
            }
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(attribute)));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean initialized() throws Exception {
        if (EncryptionSettings.SECRET == null || EncryptionSettings.ALGORITHM == null) {
            return false;
        }
        if (key == null || cipher == null) {
            key = new SecretKeySpec(EncryptionSettings.SECRET.getBytes(), EncryptionSettings.ALGORITHM);
            cipher = Cipher.getInstance(EncryptionSettings.ALGORITHM);
        }
        return true;
    }

}
