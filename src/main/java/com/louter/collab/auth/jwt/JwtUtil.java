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

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if (authentication != null) {
            System.out.println(authentication.getName());
            return authentication.getName();
        }
        return null;
    }
}
