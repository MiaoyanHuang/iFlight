package com.example.flight.utils;

import java.security.MessageDigest;
/**
 * Reference Function: MD5 Encrypt
 * Reference From: New API Company(Vero) Given
 */
public class MD5EncryptUtils {
    public static String encrypt(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] messageDigestByte = messageDigest.digest(input.getBytes());
            return bytesToHex(messageDigestByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
        }
        return hexString.toString();
    }
}
