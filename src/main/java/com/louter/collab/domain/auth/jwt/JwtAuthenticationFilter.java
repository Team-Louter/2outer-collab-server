package com.louter.collab.domain.auth.jwt;

import com.louter.collab.global.common.exception.IllegalArgumentException;
import com.louter.collab.global.common.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtAuthenticationFilter extends JwtUtil{

    public JwtAuthenticationFilter(String secretKey, long expiration) {
        super(secretKey, expiration);
    }

    public Long userIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public void validateToken(String token) {
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e){
            throw new TokenExpiredException(e.getMessage());
        } catch (JwtException | IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
