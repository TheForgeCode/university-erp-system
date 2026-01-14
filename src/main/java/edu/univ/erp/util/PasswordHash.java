package edu.univ.erp.util;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordHash {

    private static String sha256(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String hash(String plain) {
        // could add a salt here if needed, but assignment does not require it
        return sha256(plain);
    }

    public static boolean verify(String plain, String hashed) {
        if (hashed == null) return false;
        return sha256(plain).equals(hashed);
    }
}