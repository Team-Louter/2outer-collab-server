package com.louter.collab.auth.jwt;

import com.louter.collab.common.exception.IllegalArgumentException;
import com.louter.collab.common.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilter extends JwtUtil{

    public JwtAuthenticationFilter(String secretKey, long expiration) {
        super(secretKey, expiration);
    }

    public String userEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
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
