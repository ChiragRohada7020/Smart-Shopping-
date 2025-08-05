package com.cdac.dto;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class TokenStore {

    public static class TempTokenData {
        private String email;
        private String token;
        private LocalDateTime expiry;

        public TempTokenData(String email, String token, LocalDateTime expiry) {
            this.email = email;
            this.token = token;
            this.expiry = expiry;
        }

        public String getEmail() { return email; }
        public String getToken() { return token; }
        public LocalDateTime getExpiry() { return expiry; }
    }

    // key = token, value = token data
    private static final ConcurrentHashMap<String, TempTokenData> tokenMap = new ConcurrentHashMap<>();

    public static void save(String token, TempTokenData data) {
        tokenMap.put(token, data);
    }

    public static TempTokenData get(String token) {
        return tokenMap.get(token);
    }

    public static void remove(String token) {
        tokenMap.remove(token);
    }

    public static boolean isValid(String token) {
        TempTokenData data = tokenMap.get(token);
        return data != null && data.getExpiry().isAfter(LocalDateTime.now());
    }
}