package hmy.fyp.flight.utils;

import java.security.MessageDigest;

/**
 * Encryption class
 * This class is used to encrypt a string using the SHA-256 algorithm.
 *
 * @author Huang Miaoyan
 */
public class EncryptUtil {

    public static final String MD5 = "MD5";
    public static final String SHA_256 = "SHA-256";

    /**
     * Encrypt the input string using the SHA-256 algorithm.
     *
     * @param input The string to be encrypted
     * @return The encrypted string
     */
    public static String encrypt(String input, String encryptType) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(encryptType);
            byte[] messageDigestByte = messageDigest.digest(input.getBytes());
            return bytesToHex(messageDigestByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert a byte array to a hexadecimal string.
     *
     * @param bytes The byte array to be converted
     * @return The hexadecimal string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
