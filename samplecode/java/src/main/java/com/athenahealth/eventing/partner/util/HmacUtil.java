package com.athenahealth.eventing.partner.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

@Slf4j
@UtilityClass
public class HmacUtil {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public static String calculateHmac(String data, String secret) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKey);
            byte[] hmacData = mac.doFinal(data.getBytes());
            return javax.xml.bind.DatatypeConverter.printHexBinary(hmacData).toLowerCase();
        } catch (Exception ex) {
            log.error("Error while generating Hmac {}", ex);
        }
        return null;
    }

    public static boolean compareHmac(String hexSignature1, String hexSignature2, String secret)  {
        try {
            // Both inputs are already hex-encoded HMAC signatures, so compare them directly
            // Use constant-time comparison to prevent timing attacks
            if (hexSignature1 == null || hexSignature2 == null) {
                return false;
            }
            if (hexSignature1.length() != hexSignature2.length()) {
                return false;
            }
            int result = 0;
            for (int i = 0; i < hexSignature1.length(); i++) {
                result |= hexSignature1.charAt(i) ^ hexSignature2.charAt(i);
            }
            return result == 0;
        } catch (Exception ex) {
            log.error("Error while comparing Hmac {}", ex);
        }
        return false;
    }
}
