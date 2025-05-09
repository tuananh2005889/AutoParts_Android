package com.BackEnd.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class SignatureGenerator {
    public static String generateSignature(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);

        byte[] hashBytes = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
