package com.BackEnd.utils;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.TreeMap;

//public class SignatureUtil {
//
//    public static String hmacSHA256(String key, String data) throws Exception {
//        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
//        sha256_HMAC.init(secret_key);
//
//        byte[] hash = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
//        StringBuilder result = new StringBuilder();
//        for (byte b : hash) {
//            result.append(String.format("%02x", b));
//        }
//        return result.toString();
//    }
//
//    public static String createSignature(Map<String, String> params, String checksumKey) throws Exception {
//        // Sắp xếp params theo key alphabet
//        Map<String, String> sortedParams = new TreeMap<>(params);
//
//        // Nối chuỗi key=value&...
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
//            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
//                if (sb.length() > 0) sb.append("&");
//                sb.append(entry.getKey()).append("=").append(entry.getValue());
//            }
//        }
//
//        // Tạo signature
//        return hmacSHA256(checksumKey, sb.toString());
//    }
//}
//


import java.nio.charset.StandardCharsets;

public class SignatureUtil {
    public static String createSignature(Map<String, String> data, String checksumKey) throws Exception {
        Map<String, String> sorted = new TreeMap<>(data);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String rawData = sb.substring(0, sb.length() - 1); // remove last '&'

        Mac hmacSha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSha256.init(secretKey);
        byte[] hash = hmacSha256.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexResult = new StringBuilder();
        for (byte b : hash) {
            hexResult.append(String.format("%02x", b));
        }

        return hexResult.toString();
    }
}
