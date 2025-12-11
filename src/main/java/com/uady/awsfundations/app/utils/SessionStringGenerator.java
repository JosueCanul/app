package com.uady.awsfundations.app.utils;

import java.security.SecureRandom;

public class SessionStringGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateSessionString() {
        byte[] bytes = new byte[64]; // 64 bytes → 128 caracteres hex
        random.nextBytes(bytes);

        StringBuilder sb = new StringBuilder(128);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}