package com.louter.collab.auth.jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    protected final Key key;
    protected final long expiration;

    public JwtUtil(String secretKey, long expiration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getName() != null) {
            return Long.parseLong(authentication.getName());
        }
        return null;
    }
}
