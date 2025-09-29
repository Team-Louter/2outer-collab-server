package com.louter.collab.auth.jwt;

import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    protected final Key key;
    protected final long expiration;

    public JwtUtil(String secretKey, long expiration) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.expiration = expiration;
    }
}
